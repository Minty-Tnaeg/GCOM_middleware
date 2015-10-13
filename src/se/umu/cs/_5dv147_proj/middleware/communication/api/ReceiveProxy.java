package se.umu.cs._5dv147_proj.middleware.communication.api;

import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class ReceiveProxy extends AbstractProxy{
    public ReceiveProxy(String nickName) {
        super(nickName);
    }

    @Override
    public void receiveMessage(AbstractContainer ac) throws RemoteException {
        super.getCommunicationsModule().receive(ac);
    }
}
