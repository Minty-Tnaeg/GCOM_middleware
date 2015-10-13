package se.umu.cs._5dv147_proj.remotes.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameServerInterface<T extends ProxyInterface> extends Serializable, Remote {

    T joinGroup(String groupName, T leader) throws RemoteException;

    String[][] groupList() throws RemoteException;

    T assignNewLeader(String groupName, T leader) throws RemoteException;
}
