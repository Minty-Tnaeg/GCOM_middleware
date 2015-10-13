package se.umu.cs._5dv147_proj.middleware.communication.module;

import se.umu.cs._5dv147_proj.middleware.message.module.MessageModule;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractCommunication;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class BasicCommunicationModule extends AbstractCommunication {
    private MessageModule mm;
    private AbstractProxy comAPI;

    public BasicCommunicationModule(MessageModule m, AbstractProxy comAPI) {
        this.mm = m;
        this.comAPI = comAPI;
        this.comAPI.setCommunuicationModule(this);
    }

    @Override
    public void send(AbstractContainer abstractContainer, ArrayList<ProxyInterface> aps) {
        for (ProxyInterface ap : aps) {
            try {

                ap.receiveMessage(abstractContainer);

            } catch (RemoteException e) {
                Debug.getDebug().log(e);
            }
        }
        
    }

    @Override
    public void receive(AbstractContainer abstractContainer) {
        mm.queueIncomingMessage(abstractContainer);
    }
}
