package se.umu.cs._5dv147_proj.communication.api;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.communication.module.CommunicationModule;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class CommunicationAPI implements ComModuleInterface{
    private String nickName;
    private CommunicationModule cm;


    public CommunicationAPI(String nickName) {
        this.nickName = nickName;
    }

    public void setCommunuicationModule(CommunicationModule cm){
        this.cm = cm;
    }

    @Override
    public void receiveMessage(AbstractContainer ac) throws RemoteException {
        //Do com.
    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    public void sendMessage(AbstractContainer message, ComModuleInterface target) throws IOException {
        try {
            target.receiveMessage(message);
        } catch (RemoteException e) {
            throw new IOException(e);
        }
    }
}
