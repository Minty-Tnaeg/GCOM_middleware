package se.umu.cs._5dv147_proj.nameServer;

import se.umu.cs._5dv147_proj.remotes.interfaces.NameServerInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by c10mjn on 2015-09-30.
 */
public class NameServerMain {

    public static void main(String[] args) {
        String name = "NameServer";

        if(System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", "nameServer.policy");

            System.setSecurityManager(new SecurityManager());
        }

        try {
            NameServerImp<AbstractProxy> ns = new NameServerImp<>();
            NameServerInterface stub = (NameServerInterface) UnicastRemoteObject.exportObject(ns, 0);
            Registry reg = LocateRegistry.createRegistry(33401);
            reg.rebind(name, stub);

            System.err.println("NameServer started");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
