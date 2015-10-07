package se.umu.cs._5dv147_proj.settings;

import remote.objects.AbstractContainer;

import java.util.HashMap;
import java.util.List;
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

    private int minDelay;
    private int maxDelay;
    private int dropRate;
    private boolean reorder;
    private List<AbstractContainer> holdBackQueue;
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
    }

    public List<AbstractContainer> fetchHoldBackQueue(){
        return this.holdBackQueue;
    }

    public void setHoldBackQueue(List<AbstractContainer> holdBackQueue) {
        this.holdBackQueue = holdBackQueue;
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
}
