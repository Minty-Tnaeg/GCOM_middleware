package se.umu.cs._5dv147_proj.middleware.message.container;


import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c12slm on 2015-10-05.
 */
public class UnorderedContainer extends AbstractContainer implements Serializable {
    public UnorderedContainer(AbstractMessage message, UUID pid){
        super(message, pid);
    }

    @Override
    public boolean isDeliverable(HashMap<UUID, Integer> hashMap, UUID pid) {
        return true;
    }

    @Override
    public boolean isRepeat(HashMap<UUID, Integer> hashMap , UUID pid) {
        return false;
    }
}
