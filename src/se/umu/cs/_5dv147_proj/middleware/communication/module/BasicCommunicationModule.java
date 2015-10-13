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
    private int sendCounter;

    public BasicCommunicationModule(MessageModule m, AbstractProxy comAPI) {
        this.mm = m;
        this.comAPI = comAPI;
        this.comAPI.setCommunuicationModule(this);
        this.sendCounter = 0;
    }

    @Override
    public void send(AbstractContainer abstractContainer, ArrayList<ProxyInterface> aps) {
        for (ProxyInterface ap : aps) {
            try {
                this.sendCounter++;
                ap.receiveMessage(abstractContainer);
            } catch (RemoteException e) {
                if(!singleSend(abstractContainer, ap)){
                    Debug.getDebug().log("Unreachable client" + ap);
                    ArrayList<ProxyInterface> pi = new ArrayList<>(aps);
                    pi.remove(ap);

                    this.mm.send(ap, pi, "ERROR");
                }
            }
        }
        this.sendCounter = 0;
        
    }

    public boolean singleSend(AbstractContainer abstractContainer, ProxyInterface pi) {
        try {
            this.sendCounter++;
            pi.receiveMessage(abstractContainer);
        } catch (RemoteException e) {
            if (this.sendCounter <= 4) {
                return singleSend(abstractContainer,pi);
            } else {
                return false;
            }
        }
        this.sendCounter = 0;
        return true;


    }

    @Override
    public void receive(AbstractContainer abstractContainer) {
        mm.queueIncomingMessage(abstractContainer);
    }
}
