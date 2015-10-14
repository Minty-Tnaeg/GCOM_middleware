package se.umu.cs._5dv147_proj.middleware.groupmanagement.module;

import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.interfaces.NameServerInterface;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c10mjn on 2015-09-30.
 */
public class NameServerCom {
    private String[][] groupList;
    private ProxyInterface com;
    private NameServerInterface ns;

    public NameServerCom(String ip, int port, ProxyInterface com) {
        this.com = com;
        try {

            Registry reg = LocateRegistry.getRegistry(ip, port);
            this.ns = (NameServerInterface) reg.lookup("NameServer");
            updateGroupList();

        } catch (RemoteException e) {
            Debug.getDebug().log(e);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    public ProxyInterface joinGroup(String group) throws RemoteException {
        return this.ns.joinGroup(group, com);
    }

    public void updateGroupList() throws RemoteException {
        this.groupList = this.ns.groupList();
    }

    public ProxyInterface takeLeader(String group) throws RemoteException {
        return this.ns.assignNewLeader(group, com);
    }

    public String[][] getGroupList(){
        return this.groupList;
    }

    public boolean removeGroup(String group) throws RemoteException {
        return this.ns.removeGroup(group);
    }
}
