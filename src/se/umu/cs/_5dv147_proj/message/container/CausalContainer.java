package se.umu.cs._5dv147_proj.message.container;

import remote.objects.AbstractContainer;
import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.settings.Debug;

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
        int referenceValue = 0;
        int messageValue = 0;
        Debug.getDebug().log("UUID:" + super.getPid() + " Value: " + messageClock.get(super.getPid()));
        Debug.getDebug().log("UUID:" + super.getPid() + " Value: " + referenceClock.get(super.getPid()));
        if(pid.equals(super.getPid())) {
            return true;
        }
        for (UUID uuid : messageClock.keySet()) {


            //Match uuid integer with local integer
            if (referenceClock.containsKey(uuid)){
                messageValue = messageClock.get(uuid);
                referenceValue = referenceClock.get(uuid);

                if(uuid != super.getPid() && referenceValue >= messageValue){
                    //CHECK
                }else if(uuid == super.getPid() && (referenceValue + 1) == messageValue){
                    //CHECK
                } else{
                    return false;
                }
            } else if (messageClock.get(uuid) == 0) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isRepeat(HashMap<UUID, Integer> referenceClock, UUID pid) {
        for (UUID uuid : messageClock.keySet()) {
            if (referenceClock.containsKey(uuid) || messageClock.get(uuid) == 0) {
                if(!(this.messageClock.get(uuid) <= referenceClock.get(uuid))){
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

}
