package se.umu.cs._5dv147_proj.message.module;


import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.message.type.ElectionMessage;
import se.umu.cs._5dv147_proj.message.type.JoinMessage;
import se.umu.cs._5dv147_proj.message.type.TextMessage;
import se.umu.cs._5dv147_proj.settings.Debug;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * Created by c10mjn on 04/10/15.
 */
public class MessageModule {
    private PriorityQueue<AbstractMessage> incMessageQueue;
    private PriorityQueue<AbstractMessage> outMessageQueue;


    public MessageModule() {
        this.incMessageQueue = new PriorityQueue<>();
        this.outMessageQueue = new PriorityQueue<>();
    }

    public AbstractMessage fetchMessage() {
        return this.incMessageQueue.poll();
    }

    public <T extends AbstractMessage> void queueIncomingMessage(T message) {
            this.incMessageQueue.add(message);
    }

    public void registerListener(ActionListener e) {

    }

    public void constructMessage(int code) throws IOException {
        AbstractMessage m;
        switch (code) {
            case 1:
                m = new ElectionMessage(code);
                break;
            case 2:
                m = new JoinMessage(code);
                break;
            case 3:
                m = new TextMessage(code);
                break;
            default:
                Debug.getDebug().log("Uninplemented code: " + code);
                throw new IOException("Unsupported code " + code);
        }

        this.queueIncomingMessage(m);


    }

    public <T extends AbstractMessage> void queueOutgoingMessage(T message) {
        this.outMessageQueue.add(message);


    }



}
