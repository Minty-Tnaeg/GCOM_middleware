package se.umu.cs._5dv147_proj.groupmanagement.module;

import remote.interfaces.ComModuleInterface;
import remote.objects.ComModuleImp;

import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class GroupModule {
    private ComModuleInterface leader;
    private ComModuleImp com;
    private ArrayList<ComModuleInterface> proxyList;
    private NameServerCom ns;


    public GroupModule(String nameServerAddress, int port) throws RemoteException {
        this.proxyList = new ArrayList<>();
        this.com = new ComModuleImp();
        this.proxyList.add((ComModuleInterface) UnicastRemoteObject.exportObject(this.com, 0));


        ns = new NameServerCom(nameServerAddress, port, this.proxyList.get(0));

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

    /*********************Name server***********************/

    public void joinGroup(String s) {
        try {
            this.leader = ns.joinGroup(s);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void electLeader(String group) throws RemoteException {
        this.ns.takeLeader(group);
    }

    public String[][] getGroups() {
        try {
            ns.updateGroupList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ns.getGroupList();
    }


    /************************ Group Module *******************/

    public ArrayList<ComModuleInterface> getProxyList() {
        return this.proxyList;
    }

    public <T extends ComModuleInterface> void addMember(T newMember) {
        this.proxyList.add(newMember);
    }


}
