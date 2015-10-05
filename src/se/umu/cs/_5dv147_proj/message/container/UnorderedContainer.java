package se.umu.cs._5dv147_proj.message.container;

import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c12slm on 2015-10-05.
 */
public class UnorderedContainer extends AbstractContainer {
    public UnorderedContainer(AbstractMessage message){
        super(message);
    }

    @Override
    public boolean isDeliverable(HashMap<UUID, Integer> hashMap) {
        return true;
    }

    @Override
    public boolean isRepeat(HashMap<UUID, Integer> hashMap) {
        return false;
    }
}
