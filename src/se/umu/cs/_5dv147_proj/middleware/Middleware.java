package se.umu.cs._5dv147_proj.middleware;

import org.apache.commons.cli.ParseException;

import se.umu.cs._5dv147_proj.middleware.groupmanagement.module.GroupModule;
import se.umu.cs._5dv147_proj.middleware.message.module.MessageModule;

import se.umu.cs._5dv147_proj.middleware.message.type.*;
import se.umu.cs._5dv147_proj.middleware.settings.*;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c10mjn on 20/09/15.
 */
public class Middleware {
    private MessageModule messageModule;
    private GroupModule groupModule;
    private ArrayList<ActionListener> listeners;

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
                if (actionEvent.getActionCommand().equals("SystemMessage")) {
                    AbstractContainer c = messageModule.fetchSystemMessage();
                    AbstractMessage m = c.getMessage();

                    //Handling join messages
                    if (m.getClass() == JoinMessage.class) {
                        ProxyInterface com = ((JoinMessage) m).getProxy();
                        if(handleJoin(com, c)) {

                            ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                            for (ActionListener listener : listeners) {
                                listener.actionPerformed(ae);
                            }
                        }
                    //Handling returning messages from join
                    } else if (m.getClass() == ReturnJoinMessage.class) {
                        ReturnJoinMessage rm = ((ReturnJoinMessage) m);
                        ArrayList<ProxyInterface> proxyList = rm.getComs();
                        proxyList.forEach(this.groupModule::addMember);

                        messageModule.setSeenVector(rm.getClock());
                        messageModule.setContainerType(rm.getContainerType());

                        Debug.getDebug().setPIDtoName(rm.getPIDtoName());

                        ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                        for (ActionListener listener : listeners) {
                            listener.actionPerformed(ae);
                        }
                    //Handling election messages.
                    } else if (m.getClass() == ElectionMessage.class) {
                        //TODO :: SOMETHING
                    //Handling leave messages.
                    } else if (m.getClass() == LeaveMessage.class) {
                        ProxyInterface com = ((LeaveMessage) m).getProxy();
                        /*try {
                            if (!this.groupModule.compareProxy(com)) {*/
                                memberLeft(com);
                            /*}
                        } catch (RemoteException e) {
                            Debug.getDebug().log(e);
                        }*/
                    } else if (m.getClass() == ErrorMessage.class) {
                        Debug.getDebug().log("Got an error message");
                        ProxyInterface com = ((ErrorMessage) m).getProxy();
                        if (this.groupModule.isLeader(com)) {
                            Debug.getDebug().log("Was leader who left");
                            //The leader has crashed to election.

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

    private boolean handleJoin(ProxyInterface member, AbstractContainer c){
        if(groupModule.addMember(member)){
            try {
                Debug.getDebug().addPid(c.getPid(), member.getNickName());
            } catch (RemoteException e) {
                Debug.getDebug().addPid(c.getPid(), "Unknown User");
            }
            messageModule.addPIDtoVector(c.getPid());
            messageModule.send(member, groupModule.getProxyList(), "JOIN");
            messageModule.send(groupModule.getProxyList(), member);
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
}
