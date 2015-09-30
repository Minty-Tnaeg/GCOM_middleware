package se.umu.cs._5dv147_proj.network;


import remote.interfaces.NameServerInterface;
import remote.objects.ComModuleImp;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by c10mjn on 2015-09-30.
 */
public class NameServerCom {
    private String[] groupList;
    private ComModuleImp com;
    private NameServerInterface ns;

    public NameServerCom(String ip, int port, ComModuleImp com) {
        this.com = com;
        try {

            Registry reg = LocateRegistry.getRegistry(ip, port);
            this.ns = (NameServerInterface) reg.lookup("NameServer");
            updateGroupList();

        } catch (RemoteException e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    public void joinGroup(String group) throws RemoteException {
        ns.joinGroup(group, this.com);
    }

    public void updateGroupList() throws RemoteException {
        this.groupList = this.ns.groupList();
    }
}
