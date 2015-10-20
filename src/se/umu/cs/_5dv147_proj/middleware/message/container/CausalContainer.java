package se.umu.cs._5dv147_proj.middleware.message.container;

import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
        Debug.getDebug().log("--------------------------------------------------");
        int referenceValue = 0;
        int messageValue = 0;

        for (UUID uuid : messageClock.keySet()) {
            Debug.getDebug().log("UUID: " + uuid);
            //Match uuid integer with local integer
            if (referenceClock.containsKey(uuid)){
                Debug.getDebug().log("Referenceclock contains key.");
                messageValue = messageClock.get(uuid);
                referenceValue = referenceClock.get(uuid);

                Debug.getDebug().log("Messagevalue: " + messageValue + " - Referencevalue: " + referenceValue);
                Debug.getDebug().log((uuid != super.getPid()) + "");
                Debug.getDebug().log((referenceValue >= messageValue) + "");
                if(!uuid.equals(super.getPid()) && referenceValue >= messageValue){
                    Debug.getDebug().log("Option 1");
                }else if(uuid.equals(super.getPid()) && (referenceValue + 1) == messageValue){
                    Debug.getDebug().log("Option 2");
                } else{
                    Debug.getDebug().log("Option 3");
                    Debug.getDebug().log("--------------------------------------------------");
                    return false;
                }
            } else if (messageClock.get(uuid) == 0) {
                Debug.getDebug().log("Option 4");
                continue;
            } else {
                Debug.getDebug().log("Option 5");
                Debug.getDebug().log("--------------------------------------------------");
                return false;
            }
        }
        Debug.getDebug().log("--------------------------------------------------");
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

    @Override
    public String toString(){
        String s = "[";

        for (Map.Entry<UUID, Integer> entry : messageClock.entrySet()) {
            s += "(" + Debug.getDebug().convertPid(entry.getKey()) + "," + entry.getValue() + ") , ";
        }

        s = s.substring(0, s.length()-3) +  "]";

        return s;
    }
}
