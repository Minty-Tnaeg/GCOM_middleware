package se.umu.cs._5dv147_proj.middleware.message.module;

import se.umu.cs._5dv147_proj.middleware.communication.module.BasicCommunicationModule;
import se.umu.cs._5dv147_proj.middleware.message.container.CausalContainer;
import se.umu.cs._5dv147_proj.middleware.message.container.ContainerType;
import se.umu.cs._5dv147_proj.middleware.message.container.UnorderedContainer;
import se.umu.cs._5dv147_proj.middleware.message.type.*;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

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

    public MessageModule(AbstractProxy comAPI, ContainerType containerType,
                         String nickName) {
        this.nickName = nickName;
        this.comMod = new BasicCommunicationModule(this, comAPI);
        this.seenVector = new HashMap<>();
        this.incMessageQueue = Collections.synchronizedList(new LinkedList<>());
        this.holdBackQueue = Collections.synchronizedList(new LinkedList<>());
        this.systemMessageQueue = Collections.synchronizedList(new LinkedList<>());
        this.listeners = new ArrayList<>();
        this.containerType = containerType;
        this.middlewarePID = UUID.randomUUID();
        this.seenVector.put(this.middlewarePID, 0);
        Debug.getDebug().addPid(this.middlewarePID, nickName);
        Debug.getDebug().setHoldBackQueue(this);
        Debug.getDebug().setPID(this.middlewarePID);
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
                deliverMessage(container);
                checkHoldBackQueue();
            }else if(!container.isRepeat(seenVector, this.middlewarePID)){
                this.holdBackQueue.add(container);
            }

        }else{
            systemMessageQueue.add(container);
            ActionEvent ae = new ActionEvent(container, 0, "SystemMessage");
            for(ActionListener al : listeners){
                al.actionPerformed(ae);
            }
        }
    }

    private void deliverMessage(AbstractContainer container) {
        AbstractMessage message = container.getMessage();
        seenVector.put(container.getPid(), seenVector.get(container.getPid()) + 1);
        this.incMessageQueue.add((TextMessage) message);

        ActionEvent ae = new ActionEvent(message, 0, "TextMessage");
        for(ActionListener al : listeners){
            al.actionPerformed(ae);
        }
    }

    private void checkHoldBackQueue() {
        for (int i = 0; i < holdBackQueue.size(); i++) {
            AbstractContainer container = holdBackQueue.get(i);
            if (container.isDeliverable(this.seenVector, this.middlewarePID)) {
                deliverMessage(container);
                holdBackQueue.remove(i);
                checkHoldBackQueue();
                return;
            }
        }
    }

    public void registerListener(ActionListener al) {
        listeners.add(al);
    }

    public void send(ArrayList<ProxyInterface> members, ProxyInterface proxy){
        ReturnJoinMessage rjm = new ReturnJoinMessage(members, this.seenVector, this.containerType);
        AbstractContainer container = createContainer(rjm);
        ArrayList<ProxyInterface> single = new ArrayList<>();
        single.add(proxy);
        comMod.send(container, single);
    }

    public void send(ProxyInterface proxy,  ArrayList<ProxyInterface> proxys,String type){
        AbstractMessage message;
        switch (type){
            case "JOIN":
                Debug.getDebug().log("Sending a JOIN");
                message = new JoinMessage(proxy);
                break;
            case "LEAVE":
                Debug.getDebug().log("Sending a LEAVE");
                message = new LeaveMessage(proxy);
                break;
            case "ERROR":
                Debug.getDebug().log("Sedning ERROR");
                message = new ErrorMessage(proxy);
                break;
            case "ELECTION":
                Debug.getDebug().log("Sending ELECTION");
                message = new ElectionMessage(proxy);
                break;
            case "NEWLEADER":
                Debug.getDebug().log("Sending NEWLEADER");
                message = new NewLeaderMessage(proxy);
                break;
            default:
                Debug.getDebug().log("Sending no message");
                message = null;
        }
        if (message != null) {
            AbstractContainer container = createContainer(message);
            comMod.send(container, proxys);
        }
    }

    public void send(String textMessage, ArrayList<ProxyInterface> proxys){
        TextMessage message = new TextMessage(textMessage, this.nickName);
        AbstractContainer container = createContainer(message);

        comMod.send(container, proxys);
    }


    public void send(ProxyInterface proxy) {
        ReturnElectionMessage message = new ReturnElectionMessage();
        AbstractContainer container = createContainer(message);

        comMod.singleSend(container, proxy);
    }

    private AbstractContainer createContainer(AbstractMessage message) {
        Debug.getDebug().log(containerType + "");
        switch(containerType) {
            case Causal:
                HashMap<UUID, Integer> localCopy = null;
                if (message.getClass() == TextMessage.class) {
                    Debug.getDebug().log(this.middlewarePID + "");
                    Debug.getDebug().log(this.seenVector.get(this.middlewarePID) + "");
                    localCopy = (HashMap<UUID, Integer>) this.seenVector.clone();
                    localCopy.put(this.middlewarePID, this.seenVector.get(this.middlewarePID) + 1);
                }
                return new CausalContainer(message, localCopy, this.middlewarePID);
            case Unordered:
                return new UnorderedContainer(message, this.middlewarePID);
            default:
                return new UnorderedContainer(message,this.middlewarePID);
        }
    }

    public List<AbstractContainer> getHoldBackQueueCopy() {
        return this.holdBackQueue;
    }

    public HashMap<UUID, Integer> getSeenVectorClockCopy() {
        return this.seenVector;
    }

    public void setSeenVector(HashMap<UUID, Integer> seenVector) {
        this.seenVector = seenVector;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    public void addPIDtoVector(UUID pid) {
        this.seenVector.put(pid, 0);
    }
}
