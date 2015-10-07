package se.umu.cs._5dv147_proj.groupmanagement.module;


import remote.interfaces.ComModuleInterface;
import remote.interfaces.NameServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c10mjn on 2015-09-30.
 */
public class NameServerCom {
    private String[][] groupList;
    private ComModuleInterface com;
    private NameServerInterface ns;

    public NameServerCom(String ip, int port, ComModuleInterface com) {
        this.com = com;
        try {

            Registry reg = LocateRegistry.getRegistry(ip, port);
            this.ns = (NameServerInterface) reg.lookup("NameServer");
            updateGroupList();

        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    public ComModuleInterface joinGroup(String group) throws RemoteException {
        return this.ns.joinGroup(group, com);
    }

    public void updateGroupList() throws RemoteException {
        this.groupList = this.ns.groupList();
    }

    public <T extends ComModuleInterface> T takeLeader(String group) throws RemoteException {
        return (T) this.ns.assignNewLeader(group, com);
    }

    public String[][] getGroupList(){
        return this.groupList;
    }
}
