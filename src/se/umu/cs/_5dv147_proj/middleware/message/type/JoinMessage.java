package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class JoinMessage extends AbstractMessage {
    private ProxyInterface newMember;


    public JoinMessage(ProxyInterface newMember, ProxyInterface sender) {
        super(sender);
        this.newMember = newMember;
    }

    public ProxyInterface getNewMember(){
        return this.newMember;
    }
}
