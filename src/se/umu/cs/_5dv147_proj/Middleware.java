package se.umu.cs._5dv147_proj;

import org.apache.commons.cli.ParseException;

import remote.interfaces.ComModuleInterface;

import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.groupmanagement.module.GroupModule;
import se.umu.cs._5dv147_proj.message.container.ContainerType;
import se.umu.cs._5dv147_proj.message.module.MessageModule;

import se.umu.cs._5dv147_proj.message.type.ElectionMessage;
import se.umu.cs._5dv147_proj.message.type.JoinMessage;
import se.umu.cs._5dv147_proj.message.type.TextMessage;
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
        this.listeners = new ArrayList<>();
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "middle.policy");
            System.setProperty("java.rmi.server.codebase", "file:/home/c10/c10mjn/edu/5dv147/assignments/project/GCOM_middleware/out/production/GCOM_middleware/se/umu/cs/_5dv147_proj/modules/");
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ClientCommandLine cli = new ClientCommandLine(args);
            this.groupModule = new GroupModule(cli.nameserverAdress, Integer.parseInt(cli.nameserverPort), cli.nickName);
            this.messageModule = new MessageModule(this.groupModule.getCommunicationAPI(), ContainerType.Unordered);
            this.listeners = new ArrayList<>();
            this.messageModule.registerListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.err.println("Event: " + actionEvent.getSource() + "Cause:" + actionEvent.getActionCommand());
                    if (actionEvent.getActionCommand().equals("SystemMessage")) {
                        AbstractMessage m = messageModule.fetchSystemMessage();
                        if (m.getClass() == JoinMessage.class) {
                            ComModuleInterface com = ((JoinMessage) m).getProxy();
                            addMember(com);

                        } else if (m.getClass() == ElectionMessage.class) {
                            //TODO :: SOMETHING
                        }
                    } else if(actionEvent.getActionCommand().equals("TextMessage")) {
                        AbstractMessage m = messageModule.fetchTextMessage();
                        if (m.getClass() == TextMessage.class) {
                            TextMessage tm = (TextMessage) m;
                            System.err.println(tm.getMessage());


                        } else if (m.getClass() == ElectionMessage.class) {
                            //TODO :: SOMETHING
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

    public void registerActionListener(ActionListener e){
        Debug.getDebug().log("Listner added " + e.toString());
        this.listeners.add(e);
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

    public String[][] getGroupList() {
        try {
            return this.groupModule.getGroups();
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
        return null;

    }

    public <T extends ComModuleInterface> void addMember(T member){

        if(groupModule.addMember(member)){

            messageModule.send(member, groupModule.getProxyList());
        }

    }

    public <T extends ComModuleInterface> void memberLeft(T member){

    }

    /* Message module */
    public void send(String text){
        messageModule.send(text, groupModule.getProxyList());
    }

    public String receive(){
        return messageModule.fetchTextMessage().getMessage();
    }

    public void addMessageListener(ActionListener e) {
        this.messageModule.registerListener(e);
    }
}
