package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.middleware.message.container.ContainerType;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
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
    private ContainerType containerType;
    private HashMap<UUID, String> PIDtoName;

    public ReturnJoinMessage(ArrayList<ProxyInterface> coms, HashMap<UUID, Integer> leaderClock, ContainerType containerType) {
        this.coms = coms;
        this.leaderClock = leaderClock;
        this.containerType = containerType;
        this.PIDtoName = Debug.getDebug().getPIDtoName();
    }

    public ArrayList<ProxyInterface> getComs() {
        return coms;
    }

    public HashMap<UUID, Integer> getClock(){
        return leaderClock;
    }

    public ContainerType getContainerType() {
        return this.containerType;
    }

    public HashMap<UUID, String> getPIDtoName() {
        return PIDtoName;
    }
}
