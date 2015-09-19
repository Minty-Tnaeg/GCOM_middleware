package se.umu.cs._5dv147_proj_.settings;

/**
 * Created by c10mjn on 19/09/15.
 */
public class Debug {
    private boolean isEnabled = false;

    private static Debug instance = null;

    private Debug() {

    }

    public static Debug getDebug(){
        if (instance == null) {
            instance = new Debug();
        }
        return instance;
    }


    public void enableDebug(){
        this.isEnabled = true;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void log(String message) {
        System.err.println("Debug: " + message);
    }

    public void log(Exception e) {
        System.err.println("Exception: " + e.getMessage());
    }


}
