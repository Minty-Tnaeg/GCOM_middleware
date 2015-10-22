package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c10mjn on 2015-10-07.
 */
public class LeaveMessage extends AbstractMessage {
    private ProxyInterface leaver;


    public LeaveMessage(ProxyInterface leaver, ProxyInterface sender) {
        super(sender);
        this.leaver = leaver;
    }

    public ProxyInterface getLeaver(){
        return this.leaver;
    }

}
