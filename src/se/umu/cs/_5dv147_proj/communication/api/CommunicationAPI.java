package se.umu.cs._5dv147_proj.communication.api;

import remote.interfaces.ComModuleInterface;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class CommunicationAPI implements ComModuleInterface{
    private String message;
    private String nickName;


    public CommunicationAPI(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public void receiveMessage(String m) throws RemoteException {
        this.message = new String(m);
        System.err.println("Message received "  + this.message);
    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    public void sendMessage(String m, ComModuleInterface target) throws IOException {
        try {
            target.receiveMessage(m);
        } catch (RemoteException e) {
            throw new IOException(e);
        }
    }

    public String readMessage() {
        return this.message;
    }
}
