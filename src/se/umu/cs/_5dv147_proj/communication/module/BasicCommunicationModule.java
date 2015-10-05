package se.umu.cs._5dv147_proj.communication.module;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractCommunication;
import remote.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.communication.api.ReceiveProxy;
import se.umu.cs._5dv147_proj.message.module.MessageModule;
import se.umu.cs._5dv147_proj.settings.Debug;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class BasicCommunicationModule extends AbstractCommunication {
    private MessageModule mm;
    private ReceiveProxy comAPI;

    public BasicCommunicationModule(MessageModule m, ReceiveProxy comAPI) {
        this.mm = m;
        this.comAPI = comAPI;
        this.comAPI.setCommunuicationModule(this);
    }

    @Override
    public void send(AbstractContainer abstractContainer, ArrayList<ComModuleInterface> comModuleInterfaces) {
        for (ComModuleInterface comModuleInterface : comModuleInterfaces) {
            try {

                comModuleInterface.receiveMessage(abstractContainer);

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
