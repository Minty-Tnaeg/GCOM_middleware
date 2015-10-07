package se.umu.cs._5dv147_proj.message.type;

import remote.objects.AbstractMessage;
import se.umu.cs._5dv147_proj.settings.Debug;

/**
 * Created by c10mjn on 04/10/15.
 */
public class TextMessage extends AbstractMessage {
    private final String message;
    private final long time;
    private final String name;

    public TextMessage(String message, String name) {
        super();
        this.message = message;
        this.name = name;
        time = System.currentTimeMillis();
    }

    public String getName(){
        return this.name;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public String toString() {
        return "[" + String.format("%1$TT", time) + "] " + name + ": " + message;
    }
}
