package se.umu.cs._5dv147_proj.remotes.interfaces;

import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-01.
 */
public interface ProxyInterface extends Remote, Serializable {

    abstract void receiveMessage(AbstractContainer m) throws RemoteException;
    String getNickName() throws RemoteException;
    long ping() throws RemoteException;
}

