package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-06.
 */
public class ReturnJoinMessage extends AbstractMessage {
    private ArrayList<ComModuleInterface> coms;

    public ReturnJoinMessage(ArrayList<ComModuleInterface> coms) {
        this.coms = coms;
    }

    public ArrayList<ComModuleInterface> getComs() {
        return coms;
    }
}
