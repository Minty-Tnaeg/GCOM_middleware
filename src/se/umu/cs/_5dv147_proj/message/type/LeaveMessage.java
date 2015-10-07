package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

/**
 * Created by c10mjn on 2015-10-07.
 */
public class LeaveMessage extends AbstractMessage {
    private ComModuleInterface com;


    public LeaveMessage(ComModuleInterface com) {
        super();
        this.com = com;
    }

    public ComModuleInterface getProxy(){
        return this.com;
    }

}
