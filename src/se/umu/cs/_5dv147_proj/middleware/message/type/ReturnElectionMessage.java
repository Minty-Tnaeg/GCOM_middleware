package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c12slm on 2015-10-14.
 */
public class ReturnElectionMessage extends AbstractMessage{
    public ReturnElectionMessage(ProxyInterface sender){
        super(sender);
    }
}
