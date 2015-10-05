package se.umu.cs._5dv147_proj.communication.module;

import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.communication.api.CommunicationAPI;
import se.umu.cs._5dv147_proj.message.module.MessageModule;


/**
 * Created by c10mjn on 2015-10-05.
 */
public class CommunicationModule {
    private MessageModule mm;

    public CommunicationModule(MessageModule m, CommunicationAPI comAPI) {
        this.mm = m;

    }

    public <T extends AbstractMessage> void MultiCast(T message) {

    }

    public <T extends AbstractMessage> T receive() {
        return null;
    }
}
