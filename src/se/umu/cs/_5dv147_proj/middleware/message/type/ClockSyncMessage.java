package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c12slm on 2015-10-23.
 */
public class ClockSyncMessage extends AbstractMessage {
    public ClockSyncMessage(ProxyInterface sender) {
        super(sender);
    }
}
