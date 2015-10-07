package se.umu.cs._5dv147_proj;

import org.apache.commons.cli.ParseException;

import remote.interfaces.ComModuleInterface;

import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.groupmanagement.module.GroupModule;
import se.umu.cs._5dv147_proj.message.container.ContainerType;
import se.umu.cs._5dv147_proj.message.module.MessageModule;

import se.umu.cs._5dv147_proj.message.type.ElectionMessage;
import se.umu.cs._5dv147_proj.message.type.JoinMessage;
import se.umu.cs._5dv147_proj.message.type.ReturnJoinMessage;
import se.umu.cs._5dv147_proj.settings.*;

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
                    if (m.getClass() == JoinMessage.class) {

                        ComModuleInterface com = ((JoinMessage) m).getProxy();
                        if(handleJoin(com)) {
                            ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                            for (ActionListener listener : listeners) {
                                listener.actionPerformed(ae);
                            }
                        }
                    } else if (m.getClass() == ReturnJoinMessage.class) {
                        ReturnJoinMessage rm = ((ReturnJoinMessage) m);
                        ArrayList<ComModuleInterface> proxyList = rm.getComs();
                        proxyList.forEach(this.groupModule::addMember);
                        messageModule.setSeenVector(rm.getClock());
                        ActionEvent ae = new ActionEvent(m, 0, "UpdateUsers");
                        for (ActionListener listener : listeners) {
                            listener.actionPerformed(ae);
                        }
                    } else if (m.getClass() == ElectionMessage.class) {
                        //TODO :: SOMETHING
                    }

                }
            });
        } catch (ParseException e) {
            Debug.getDebug().log(e);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

    private <T extends ComModuleInterface> boolean handleJoin(T member){
        if(groupModule.addMember(member)){
            messageModule.send(member, groupModule.getProxyList());
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
            ArrayList<ComModuleInterface> leader = new ArrayList<>();
            leader.add(this.groupModule.joinGroup(group));
            messageModule.send(groupModule.getCommunicationAPI(), leader);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

    public ArrayList<ComModuleInterface> getProxyList() {
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

    public <T extends ComModuleInterface> void memberLeft(T member){

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
}
