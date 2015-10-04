package se.umu.cs._5dv147_proj.message.module;

import se.umu.cs._5dv147_proj.message.type.AbstractMessage;

import java.awt.event.ActionListener;
import java.util.PriorityQueue;

/**
 * Created by c10mjn on 04/10/15.
 */
public class MessageModule {
    private PriorityQueue<AbstractMessage> name;


    public MessageModule() {
        this.name = new PriorityQueue<>();
    }

    public AbstractMessage FetchMessage() {
        return null;
    }

    public <T extends AbstractMessage> void queueIncommingMessage(T message) {
            this.name.add(message);
    }

    public void RegisterListener(ActionListener e) {

    }

    public void ConstructMessage(int code, String message) {

    }

    public <T extends AbstractMessage> void queueOutgoingMessage(T message) {

    }

}
