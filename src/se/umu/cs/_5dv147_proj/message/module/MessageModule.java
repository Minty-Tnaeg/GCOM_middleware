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
import se.umu.cs._5dv147_proj.message.type.TextMessage;

import javax.xml.soap.Text;
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
    private PriorityBlockingQueue<AbstractMessage> systemMessageQueue;
    private ArrayList<ActionListener> listeners;
    private ContainerType containerType;

    public MessageModule(CommunicationAPI comAPI, ContainerType containerType) {
        this.comMod = new BasicCommunicationModule(this, comAPI);
        this.containerType = containerType;
        this.comMod = new CommunicationModule(this, comAPI);
        this.incMessageQueue = new PriorityBlockingQueue<>();
        this.holdBackQueue = new PriorityBlockingQueue<>();
        this.systemMessageQueue = new PriorityBlockingQueue<>();
    }

    public AbstractMessage fetchMessage() {
        return this.incMessageQueue.poll();
    }

    public void queueIncomingMessage(AbstractContainer container) {
        AbstractMessage message = container.getMessage();

        if(message.getClass() == TextMessage.class){
            if(container.isDeliverable(seenVector)){
                this.incMessageQueue.add(message);

                ActionEvent ae = new ActionEvent(null, 0, "TextMessage");
                for(ActionListener al : listeners){
                    al.actionPerformed(ae);
                }
                checkHoldBackQueue();

            }else if(!container.isRepeat(seenVector)){
                this.holdBackQueue.put(container);
            }

        }else{
            systemMessageQueue.put(message);
            ActionEvent ae = new ActionEvent(null, 0, "SystemMessage");
            for(ActionListener al : listeners){
                al.actionPerformed(ae);
            }
        }
    }

    private void checkHoldBackQueue() {
        //TODO :: CHECK HOLDBACK QUEUE FOR NEW MESSAGES ABLE TO DELIVER
    }

    public void registerListener(ActionListener al) {
        listeners.add(al);
    }

    public void send(ComModuleInterface newMember,  ArrayList<ComModuleInterface> proxys){
        JoinMessage message = new JoinMessage(newMember);
        AbstractContainer container;

        if(containerType == ContainerType.Causal){
            container = new CausalContainer(message);
        }else if(containerType == ContainerType.Unordered){
            container = new UnorderedContainer(message);
        }

        comMod.multiCast(container, proxys);
    }

    public void send(String textMessage, ArrayList<ComModuleInterface> proxys){
        TextMessage message = new TextMessage(textMessage);
        AbstractContainer container;

        if(containerType == ContainerType.Causal){
            container = new CausalContainer(message);
        }else if(containerType == ContainerType.Unordered){
            container = new UnorderedContainer(message);
        }

        comMod.multiCast(container, proxys);
    }
}
