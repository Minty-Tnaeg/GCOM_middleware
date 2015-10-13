package se.umu.cs._5dv147_proj.remotes.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c12slm on 2015-10-05.
 */
public abstract class AbstractContainer implements Serializable {
    private AbstractMessage message;
    private UUID pid;

    public AbstractContainer(AbstractMessage message, UUID pid) {
        this.message = message;
        this.pid = pid;
    }

    public AbstractMessage getMessage(){
        return this.message;
    }

    public UUID getPid(){
        return this.pid;
    }

    @Override
    public String toString(){
        return "[No vector]";
    }

    abstract public boolean isDeliverable(HashMap<UUID, Integer> vectorClock, UUID pid);

    abstract public boolean isRepeat(HashMap<UUID, Integer> vectorClock, UUID pid);
}
