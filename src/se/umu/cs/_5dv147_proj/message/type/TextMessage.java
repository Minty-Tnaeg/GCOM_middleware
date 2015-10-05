package se.umu.cs._5dv147_proj.message.type;

import remote.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class TextMessage extends AbstractMessage {
    private final String message;

    public TextMessage(String message) {
        super();
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public void method() {

    }
}
