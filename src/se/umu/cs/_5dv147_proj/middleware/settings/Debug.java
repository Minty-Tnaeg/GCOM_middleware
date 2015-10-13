package se.umu.cs._5dv147_proj.middleware.settings;

import se.umu.cs._5dv147_proj.middleware.message.module.MessageModule;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class Debug {
    /**
     * Created by c10mjn on 19/09/15.
     */
    private boolean isEnabled = false;

    private static Debug debugInstance = null;

    private int minDelay = 0;
    private int maxDelay = 1;
    private int dropRate = 0;
    private boolean reorder;
    private MessageModule mm;
    private HashMap<UUID, String> pidToName;

    private Debug() {
        pidToName = new HashMap<>();
    }

    public static Debug getDebug(){
        if (debugInstance == null) {
            debugInstance = new Debug();
        }
        return debugInstance;
    }


    public void enableDebug(){
        this.isEnabled = true;
    }

    public void log(String message) {
        if (this.isEnabled) {
            System.err.println("Debug: " + message);
        }
    }

    public void log(Exception e) {
        log("Exception: " + e.getMessage());
        e.printStackTrace();
    }

    public List<AbstractContainer> fetchHoldBackQueue(){
        return this.mm.getHoldBackQueueCopy();
    }

    public void setHoldBackQueue(MessageModule mm) {
        this.mm = mm;
    }

    public boolean shouldReorder() {
        return reorder;
    }

    public int getDropRate() {
        return dropRate;
    }

    public int getMinDelay() {
        return minDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setDelay(int min, int max) {
        if(isEnabled) {
            this.minDelay = min;
            this.maxDelay = max;
        }
    }

    public void setDropRate(int dropRate) {
        if(isEnabled) {
            this.dropRate = dropRate;
        }
    }

    public void setReorder(boolean reorder) {
        if(isEnabled){
            this.reorder = reorder;
        }
    }

    public void addPid(UUID key, String name){
        if(isEnabled){
            pidToName.put(key, name);
        }
    }

    public String convertPid(UUID key) {
        if(isEnabled){
            return pidToName.get(key) == null ? "Unknown user" : pidToName.get(key);
        }
        else{
            return "Unknown user";
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public HashMap<UUID, Integer> getCompareVectorClock() {
        return this.mm.getSeenVectorClockCopy();
    }

    public HashMap<UUID, String> getPIDtoName() {
        if(isEnabled){
            return this.pidToName;
        }else{
            return null;
        }
    }

    public void setPIDtoName(HashMap<UUID, String> PIDtoName) {
        for(Map.Entry e : PIDtoName.entrySet()){
            Debug.getDebug().log(e.getKey() + " - " + e.getValue());
        }

        if(isEnabled){
            this.pidToName = PIDtoName;
        }
    }
}
