package se.umu.cs._5dv147_proj.message.module;


import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.communication.api.ReceiveProxy;
import se.umu.cs._5dv147_proj.communication.module.BasicCommunicationModule;
import se.umu.cs._5dv147_proj.message.container.CausalContainer;
import se.umu.cs._5dv147_proj.message.container.ContainerType;
import se.umu.cs._5dv147_proj.message.container.UnorderedContainer;
import se.umu.cs._5dv147_proj.message.type.JoinMessage;
import se.umu.cs._5dv147_proj.message.type.ReturnJoinMessage;
import se.umu.cs._5dv147_proj.message.type.TextMessage;
import se.umu.cs._5dv147_proj.settings.Debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by c10mjn on 04/10/15.
 */
public class MessageModule {
    private String nickName;
    private HashMap<UUID, Integer> seenVector;
    private BasicCommunicationModule comMod;
    private List<TextMessage> incMessageQueue;
    private List<AbstractContainer> holdBackQueue;
    private List<AbstractContainer> systemMessageQueue;
    private ArrayList<ActionListener> listeners;
    private ContainerType containerType;
    private UUID middlewarePID;

    public MessageModule(ReceiveProxy comAPI, ContainerType containerType,
                         String nickName) {
        this.nickName = nickName;
        this.comMod = new BasicCommunicationModule(this, comAPI);
        this.comMod = new BasicCommunicationModule(this, comAPI);
        this.seenVector = new HashMap<>();
        this.incMessageQueue = Collections.synchronizedList(new LinkedList<>());
        this.holdBackQueue = Collections.synchronizedList(new LinkedList<>());
        this.systemMessageQueue = Collections.synchronizedList(new LinkedList<>());
        this.listeners = new ArrayList<>();
        this.containerType = containerType;
        this.middlewarePID = UUID.randomUUID();
        this.seenVector.put(this.middlewarePID, 0);

        Debug.getDebug().setHoldBackQueue(this.holdBackQueue);
    }

    public TextMessage fetchTextMessage() {
        TextMessage m = this.incMessageQueue.get(0);
        this.incMessageQueue.remove(0);
        return m;
    }

    public AbstractContainer fetchSystemMessage() {
        AbstractContainer m = this.systemMessageQueue.get(0);
        this.systemMessageQueue.remove(0);
        return m;
    }

    public void queueIncomingMessage(AbstractContainer container) {
        AbstractMessage message = container.getMessage();

        if(message.getClass() == TextMessage.class){
            if(container.isDeliverable(seenVector, this.middlewarePID)){

                if (!(container.getPid().equals(this.middlewarePID))) {
                    if(seenVector.get(container.getPid()) == null){
                        seenVector.put(container.getPid(), 1);
                        Debug.getDebug().addPid(container.getPid(), ((TextMessage)container.getMessage()).getName());
                    }else{
                        seenVector.put(container.getPid(), seenVector.get(container.getPid()) + 1);
                    }
                }

                this.incMessageQueue.add((TextMessage) message);

                ActionEvent ae = new ActionEvent(message, 0, "TextMessage");
                for(ActionListener al : listeners){
                    al.actionPerformed(ae);
                }
                checkHoldBackQueue();

            }else if(!container.isRepeat(seenVector, this.middlewarePID)){
                this.holdBackQueue.add(container);
                ActionEvent ae = new ActionEvent(message, 0, "holdBack");
                for(ActionListener al : listeners){
                    al.actionPerformed(ae);
                }
            }

        }else{

            systemMessageQueue.add(container);
            ActionEvent ae = new ActionEvent(container, 0, "SystemMessage");
            for(ActionListener al : listeners){
                al.actionPerformed(ae);
            }
        }
    }

    private void checkHoldBackQueue() {
        for (int i = 0; i < holdBackQueue.size(); i++) {
            AbstractContainer container = holdBackQueue.get(i);
            if (container.isDeliverable(this.seenVector, this.middlewarePID)) {
                seenVector.put(container.getPid(), seenVector.get(container.getPid()) == null ? 0 : seenVector.get(container.getPid()) + 1);
                this.incMessageQueue.add((TextMessage) container.getMessage());
                this.holdBackQueue.remove(container);

                ActionEvent ae = new ActionEvent(container.getMessage(), 0, "TextMessage");
                for (ActionListener al : listeners) {
                    al.actionPerformed(ae);
                }

                i = 0;
            }
        }
    }

    public void registerListener(ActionListener al) {
        listeners.add(al);
    }

    public void send(ArrayList<ComModuleInterface> members, ComModuleInterface proxy){
        ReturnJoinMessage rjm = new ReturnJoinMessage(members);
        AbstractContainer container = createContainer(rjm);
        ArrayList<ComModuleInterface> single = new ArrayList<>();
        single.add(proxy);
        comMod.send(container, single);
    }

    public void send(ComModuleInterface newMember,  ArrayList<ComModuleInterface> proxys){
        JoinMessage message = new JoinMessage(newMember);
        AbstractContainer container = createContainer(message);

        comMod.send(container, proxys);

    }

    public void send(String textMessage, ArrayList<ComModuleInterface> proxys){
        TextMessage message = new TextMessage(textMessage, this.nickName);
        AbstractContainer container = createContainer(message);

        comMod.send(container, proxys);
    }

    private AbstractContainer createContainer(AbstractMessage message) {
        switch(containerType) {
            case Causal:
                if (message.getClass() == TextMessage.class) {
                    this.seenVector.put(this.middlewarePID, this.seenVector.get(this.middlewarePID) + 1);
                }
                return new CausalContainer(message, this.seenVector, this.middlewarePID);
            case Unordered:
                return new UnorderedContainer(message, this.middlewarePID);
            default:
                return new UnorderedContainer(message,this.middlewarePID);
        }
    }
}
