package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class JoinMessage extends AbstractMessage {
    private ComModuleInterface com;

    public JoinMessage(ComModuleInterface com) {
        super();
        this.com = com;
    }

    public ComModuleInterface getProxy(){
        return this.com;
    }
}
