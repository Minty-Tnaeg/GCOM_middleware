package se.umu.cs._5dv147_proj.message.module;


import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.communication.api.CommunicationAPI;
import se.umu.cs._5dv147_proj.communication.module.BasicCommunicationModule;

import se.umu.cs._5dv147_proj.message.container.CausalContainer;
import se.umu.cs._5dv147_proj.message.container.ContainerType;
import se.umu.cs._5dv147_proj.message.container.UnorderedContainer;
import se.umu.cs._5dv147_proj.message.type.JoinMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by c10mjn on 04/10/15.
 */
public class MessageModule {
    private HashMap<UUID, Integer> seenVector;
    private BasicCommunicationModule comMod;
    private PriorityBlockingQueue<AbstractContainer> incMessageQueue;
    private PriorityBlockingQueue<AbstractContainer> holdBackQueue;
    private ArrayList<ActionListener> listeners;
    private ContainerType type;

    public MessageModule(CommunicationAPI comAPI, ContainerType containerType) {
        this.comMod = new BasicCommunicationModule(this, comAPI);
        this.incMessageQueue = new PriorityBlockingQueue<>();
        this.holdBackQueue = new PriorityBlockingQueue<>();
    }

    public AbstractMessage fetchMessage() {
        return this.incMessageQueue.poll().getMessage();
    }

    public void queueIncomingMessage(AbstractContainer message) {
        if(message.isDeliverable(seenVector)){
            this.incMessageQueue.add(message);
            ActionEvent ae = new ActionEvent(null, 0, "");
            for(ActionListener al : listeners){
                al.actionPerformed(ae);
            }
            checkHoldBackQueue();
        }else if(!message.isRepeat(seenVector)){
            this.holdBackQueue.put(message);
        }
    }

    private void checkHoldBackQueue() {
        //TODO :: CHECK HOLDBACK QUEUE FOR NEW MESSAGES ABLE TO DELIVER
    }

    public void registerListener(ActionListener al) {
        listeners.add(al);
    }

    public void send(ComModuleInterface newMember,  ArrayList<ComModuleInterface> proxys){
        JoinMessage message = new JoinMessage(1);
        AbstractContainer container;

        if(type == ContainerType.Causal){
            container = new CausalContainer(message);
        }else if(type == ContainerType.Unordered){
            container = new UnorderedContainer(message);
        }

        //comMod.send(THING);
    }

    public void send(String textMessage, ArrayList<ComModuleInterface> proxys){

        //comMod.send(THING);
    }
}
