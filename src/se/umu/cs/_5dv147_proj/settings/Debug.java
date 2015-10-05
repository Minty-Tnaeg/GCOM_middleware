package se.umu.cs._5dv147_proj.settings;

/**
 * Created by c10mjn on 2015-10-05.
 */
public class Debug {
    /**
     * Created by c10mjn on 19/09/15.
     */
        private boolean isEnabled = false;

        private static Debug debugInstance = null;

        private Debug() {

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
