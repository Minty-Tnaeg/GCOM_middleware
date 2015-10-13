package se.umu.cs._5dv147_proj.remotes.objects;

import se.umu.cs._5dv147_proj.middleware.communication.module.BasicCommunicationModule;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;

import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-01.
 */
public abstract class AbstractProxy implements ProxyInterface{
    private String nickName;
    private BasicCommunicationModule cm;

    public AbstractProxy(String nickName){
        this.nickName = nickName;
    }

    abstract public void receiveMessage(AbstractContainer ac) throws RemoteException;

    public void setCommunuicationModule(BasicCommunicationModule cm){
        this.cm = cm;
    }

    public String getNickName() throws RemoteException {
        return nickName;
    }

    public long ping() throws RemoteException {
        return System.currentTimeMillis();
    }

    public BasicCommunicationModule getCommunicationsModule(){
        return this.cm;
    }
}

