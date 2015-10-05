package se.umu.cs._5dv147_proj;

import org.apache.commons.cli.ParseException;

import remote.interfaces.ComModuleInterface;

import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.groupmanagement.module.GroupModule;
import se.umu.cs._5dv147_proj.message.module.MessageModule;

import se.umu.cs._5dv147_proj.message.type.ElectionMessage;
import se.umu.cs._5dv147_proj.settings.*;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c10mjn on 20/09/15.
 */
public class Middleware {

    private String group;

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
            this.messageModule = new MessageModule(this.groupModule.getComAPI());

        } catch (ParseException e) {
            Debug.getDebug().log(e);
        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        }
    }

    public void registerActionListener(ActionListener e){
        this.listeners.add(0, e);
    }


    /* Group Module */

    public void joinGroup(String group) {
        try {
            this.groupModule.joinGroup(group);
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
        this.groupModule.addMember(member);
    }

    public <T extends ComModuleInterface> void memberLeft(T member){

    }




    /* Message Module */

    public void sendMessage(String m) throws IOException {
        this.messageModule.constructMessage(1);
    }

    public <T extends AbstractMessage> void getMessage(){
        AbstractMessage m = this.messageModule.fetchMessage();
        switch(m.getCode()) {
            case 1:
                ElectionMessage em = (ElectionMessage) m;
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                Debug.getDebug().log("Not supported code: " + m.getCode());
        }
    }

    public void addMessageListener(ActionListener e) {
        this.messageModule.registerListener(e);
    }



}
