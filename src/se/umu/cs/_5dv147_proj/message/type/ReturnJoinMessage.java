package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c10mjn on 2015-10-06.
 */
public class ReturnJoinMessage extends AbstractMessage {
    private ArrayList<ComModuleInterface> coms;
    private HashMap<UUID, Integer> leaderClock;

    public ReturnJoinMessage(ArrayList<ComModuleInterface> coms, HashMap<UUID, Integer> leaderClock) {
        this.coms = coms;
        this.leaderClock = leaderClock;
    }

    public ArrayList<ComModuleInterface> getComs() {
        return coms;
    }

    public HashMap<UUID, Integer> getClock(){
        return leaderClock;
    }
}
