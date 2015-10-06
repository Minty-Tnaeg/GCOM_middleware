package se.umu.cs._5dv147_proj.message.container;

import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by c12slm on 2015-10-05.
 */
public class CausalContainer extends AbstractContainer implements Serializable {
    private HashMap<UUID, Integer> messageClock;

    /**
     *
     * @param message content
     * @param messageClock clock from sender
     * @param pid id of sender
     */
    public CausalContainer(AbstractMessage message, HashMap<UUID, Integer> messageClock, UUID pid) {
        super(message, pid);
        this.messageClock = messageClock;
    }


    /**
     *
     * @param referenceClock [<Sid, seq#>, <Sid, seq#>]
     * @return True if sequence number matches.
     */
    @Override
    public boolean isDeliverable(HashMap<UUID, Integer> referenceClock, UUID pid) {
        for (UUID uuid : messageClock.keySet()) {
            //Match uuid integer with local integer
            if (referenceClock.containsKey(uuid)){
                if(uuid != super.getPid() && referenceClock.get(uuid) >= this.messageClock.get(uuid)){
                    //CHECK
                }else if(uuid == super.getPid() && (referenceClock.get(uuid) + 1) == this.messageClock.get(uuid)){
                    //CHECK
                }else if(pid == super.getPid() && uuid == super.getPid() && (referenceClock.get(uuid)) == this.messageClock.get(uuid)) {
                    //CHECK
                }else{
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isRepeat(HashMap<UUID, Integer> referenceClock, UUID pid) {
        for (UUID uuid : messageClock.keySet()) {
            if (referenceClock.containsKey(uuid)) {
                if(!(this.messageClock.get(uuid) <= referenceClock.get(uuid))){
                    return false;
                }
            }
        }
        return true;
    }

}
