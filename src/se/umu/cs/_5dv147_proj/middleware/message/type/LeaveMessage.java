package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

/**
 * Created by c10mjn on 2015-10-07.
 */
public class LeaveMessage extends AbstractMessage {
    private ProxyInterface com;


    public LeaveMessage(ProxyInterface com) {
        super();
        this.com = com;
    }

    public ProxyInterface getProxy(){
        return this.com;
    }

}
