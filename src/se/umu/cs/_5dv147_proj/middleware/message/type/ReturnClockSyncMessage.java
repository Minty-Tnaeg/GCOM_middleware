package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c12slm on 2015-10-23.
 */
public class ReturnClockSyncMessage extends AbstractMessage {
    private Integer sequenceNumber;

    public ReturnClockSyncMessage(ProxyInterface sender, Integer sequenceNumber) {
        super(sender);
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getSequenceNumber(){
        return this.sequenceNumber;
    }
}
