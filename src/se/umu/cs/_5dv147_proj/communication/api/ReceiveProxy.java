package se.umu.cs._5dv147_proj.communication.api;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.communication.module.BasicCommunicationModule;
import se.umu.cs._5dv147_proj.settings.Debug;

import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class ReceiveProxy implements ComModuleInterface{
    private String nickName;
    private BasicCommunicationModule cm;

    public ReceiveProxy(String nickName) {
        this.nickName = nickName;
    }

    public void setCommunuicationModule(BasicCommunicationModule cm){
        this.cm = cm;
    }

    @Override
    public void receiveMessage(AbstractContainer ac) throws RemoteException {


        cm.receive(ac);
    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }
}
