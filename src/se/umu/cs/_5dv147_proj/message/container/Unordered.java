package se.umu.cs._5dv147_proj.message.container;

import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;

/**
 * Created by c12slm on 2015-10-05.
 */
public class Unordered extends AbstractContainer {
    public Unordered(AbstractMessage message){
        super(message);
    }

    public boolean compare(AbstractContainer compare){
        return true;
    }
}
