package se.umu.cs._5dv147_proj.middleware;

import org.apache.commons.cli.ParseException;

import se.umu.cs._5dv147_proj.middleware.groupmanagement.module.GroupModule;
import se.umu.cs._5dv147_proj.middleware.message.module.MessageModule;

import se.umu.cs._5dv147_proj.middleware.message.type.*;
import se.umu.cs._5dv147_proj.middleware.settings.*;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by c10mjn on 20/09/15.
 */
public class Middleware {
    private MessageModule messageModule;
    private GroupModule groupModule;
    private ArrayList<ActionListener> listeners;
    private boolean receivedElectionResponse;

    public Middleware(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "middle.policy");
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ClientCommandLine cli = new ClientCommandLine(args);
            this.listeners = new ArrayList<>();
            this.groupModule = new GroupModule(cli.nameserverAdress, Integer.parseInt(cli.nameserverPort), cli.nickName);
            this.messageModule = new MessageModule(this.groupModule.getCommunicationAPI(), cli.containerType, cli.nickName);

            this.messageModule.registerListener(actionEvent -> {
                Debug.getDebug().log("Got message. Type is: " + actionEvent.getActionCommand());
                if (actionEvent.getActionCommand().equals("SystemMessage")) {
                    AbstractContainer c = messageModule.fetchSystemMessage();
                    AbstractMessage m = c.getMessage();

                    //Handling join messages
                    if (m.getClass() == JoinMessage.class) {
                        Debug.getDebug().log("Got JOIN message from " + c.getPid());

                        if(handleJoin(c)) {
                            ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                            for (ActionListener listener : listeners) {
                                listener.actionPerformed(ae);
                            }
                        }
                    //Handling returning messages from join
                    } else if (m.getClass() == ReturnJoinMessage.class) {
                        Debug.getDebug().log("Got returnjoin message from " + c.getPid());
                        ReturnJoinMessage rm = ((ReturnJoinMessage) m);
                        ArrayList<ProxyInterface> proxyList = rm.getComs();
                        proxyList.forEach(this.groupModule::addMember);

                        messageModule.send(null, proxyList, "CLOCKSYNC");

                        messageModule.setContainerType(rm.getContainerType());

                        Debug.getDebug().setPIDtoName(rm.getPIDtoName());

                        ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                        for (ActionListener listener : listeners) {
                            listener.actionPerformed(ae);
                        }
                    //Handling election messages.
                    } else if (m.getClass() == ClockSyncMessage.class) {
                        messageModule.send(m.getSender(), "RETURNCLOCKSYNC");
                    }else if (m.getClass() == ReturnClockSyncMessage.class) {
                        this.messageModule.setSeenVector(c.getPid(), ((ReturnClockSyncMessage)m).getSequenceNumber());
                    }else if (m.getClass() == ElectionMessage.class) {
                        messageModule.send(((ElectionMessage) m).getProxy(), "RETURNELECTIONMESSAGE");
                    }else if (m.getClass() == ReturnElectionMessage.class){
                        this.receivedElectionResponse = true;
                    //Handling leave messages.
                    } else if (m.getClass() == LeaveMessage.class) {
                        ProxyInterface com = ((LeaveMessage) m).getLeaver();
                        memberLeft(com);
                    } else if (m.getClass() == ErrorMessage.class) {
                        Debug.getDebug().log("Got an error message");
                        ProxyInterface com = ((ErrorMessage) m).getProxy();
                        if (this.groupModule.isLeader(com)) {
                            Debug.getDebug().log("Was leader who left");
                            //The leader has crashed to election.
                            messageModule.send(com, groupModule.electNewLeader(), "ELECTION");
                            receivedElectionResponse = false;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if(!receivedElectionResponse){
                                        try {
                                            groupModule.takeLeader();
                                            messageModule.send(com, groupModule.getProxyList(), "NEWLEADER");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, 15000);
                        } else {
                            Debug.getDebug().log("Was member");
                            memberLeft(com);
                        }
                    }
                }
            });
        } catch (ParseException e) {
            Debug.getDebug().log(e);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }

    }

    private boolean handleJoin(AbstractContainer c){
        JoinMessage jm = ((JoinMessage) c.getMessage());
        ProxyInterface member = jm.getNewMember();

        if(groupModule.addMember(member)){
            Debug.getDebug().log("Added member: " + c.getPid());
            try {
                Debug.getDebug().addPid(c.getPid(), member.getNickName());
            } catch (RemoteException e) {
                Debug.getDebug().addPid(c.getPid(), "Unknown User");
            }

            messageModule.addPIDtoVector(c.getPid());
            if(member.equals(jm.getSender())){
                messageModule.send(member, groupModule.getProxyList(), "RETURNJOIN");
                messageModule.send(c, groupModule.getProxyList());
            }
            return true;
        }
        return false;
    }

    public void registerActionListener(ActionListener e){
        this.listeners.add(e);
        this.messageModule.registerListener(e);
    }

    /* Group Module */

    public void joinGroup(String group) {
        try {
            ArrayList<ProxyInterface> leader = new ArrayList<>();
            leader.add(this.groupModule.joinGroup(group));
            messageModule.send(groupModule.getCommunicationAPI(), leader, "JOIN");
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

    public ArrayList<ProxyInterface> getProxyList() {
        return this.groupModule.getProxyList();
    }

    public ArrayList<String> getNameList(){
        return this.groupModule.getNameList();
    }

    public String[][] getGroupList() {
        try {
            return this.groupModule.getGroups();
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
        return null;
    }

    /**
     *
     * @param member The member who is leaving.
     * @param <T>
     */
    public <T extends ProxyInterface> void memberLeft(T member){
        try {
            this.groupModule.removeMember(member);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

    /* Message module */
    public void send(String text){
        messageModule.send(text, groupModule.getProxyList());
    }

    public String receive(){
        return messageModule.fetchTextMessage().toString();
    }

    public void addMessageListener(ActionListener e) {
        this.messageModule.registerListener(e);
    }

    public void sendLeave() throws RemoteException {
        Debug.getDebug().log("Seding a leave: " + this.groupModule.getCommunicationAPI().getNickName());
        messageModule.send(groupModule.getCommunicationAPI(), groupModule.getProxyList(), "LEAVE");

    }

    public void removeGroup(String group) {
        try {
            Debug.getDebug().log("Attempting to remove group " + group);
            this.groupModule.removeGroup(group);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

}
