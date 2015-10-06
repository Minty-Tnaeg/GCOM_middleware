package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

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

    public String getMessage(){
        System.err.println(message);
        return "[" + String.format("%1$TT", time) + "] " + name + ": " + message;
    }
}
