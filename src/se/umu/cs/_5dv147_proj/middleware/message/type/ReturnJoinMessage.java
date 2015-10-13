package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c10mjn on 2015-10-06.
 */
public class ReturnJoinMessage extends AbstractMessage {
    private ArrayList<ProxyInterface> coms;
    private HashMap<UUID, Integer> leaderClock;

    public ReturnJoinMessage(ArrayList<ProxyInterface> coms, HashMap<UUID, Integer> leaderClock) {
        this.coms = coms;
        this.leaderClock = leaderClock;
    }

    public ArrayList<ProxyInterface> getComs() {
        return coms;
    }

    public HashMap<UUID, Integer> getClock(){
        return leaderClock;
    }
}
