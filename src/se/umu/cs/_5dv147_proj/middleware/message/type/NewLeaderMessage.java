package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c12slm on 2015-10-14.
 */
public class NewLeaderMessage extends AbstractMessage {
    private ProxyInterface newLeader;

    public NewLeaderMessage(ProxyInterface newLeader, ProxyInterface sender) {
        super(sender);
        this.newLeader = newLeader;
    }

    public ProxyInterface getLeader(){
        return this.newLeader;
    }
}
