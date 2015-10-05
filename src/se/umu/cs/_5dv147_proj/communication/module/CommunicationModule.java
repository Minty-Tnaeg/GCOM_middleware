package se.umu.cs._5dv147_proj.communication.module;

import se.umu.cs._5dv147_proj.message.module.MessageModule;
import se.umu.cs._5dv147_proj.message.type.AbstractMessage;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class CommunicationModule {
    private MessageModule mm;

    public CommunicationModule(MessageModule m) {
        this.mm = m;

    }

    public <T extends AbstractMessage> void MultiCast(T message) {

    }

    public <T extends AbstractMessage> T receive() {
        return null;
    }
}
