package se.umu.cs._5dv147_proj;

import org.apache.commons.cli.ParseException;

import remote.interfaces.ComModuleInterface;
import remote.interfaces.NameServerInterface;
import remote.objects.ComModuleImp;

import se.umu.cs._5dv147_proj.message.module.MessageModule;
import se.umu.cs._5dv147_proj.network.NameServerCom;
import se.umu.cs._5dv147_proj.settings.*;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by c10mjn on 20/09/15.
 */
public class Middleware {
    private NameServerCom ns;
    private ComModuleImp com;
    private ComModuleInterface stub;
    private String group;
    private ComModuleInterface leader;
    private MessageModule mm;

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

            this.com = new ComModuleImp();
            this.stub = (ComModuleInterface) UnicastRemoteObject.exportObject(this.com, 0);
            System.err.println(this.stub);
            ns = new NameServerCom(cli.nameserverAdress, Integer.parseInt(cli.nameserverPort), this.stub);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void send(String s) {
        try {
            this.com.sendMessage(s, leader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        return com.readMessage();
    }

    public String[][] getGroups() {
        try {
            ns.updateGroupList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ns.getGroupList();
    }

    public void joinGroup(String s) {
        try {
            this.leader = ns.joinGroup(s);
            System.err.println(this.leader);
            this.group = s;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void registerActionListener(ActionListener e){
        this.listeners.add(0, e);
    }
}
