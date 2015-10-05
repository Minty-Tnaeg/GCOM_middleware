package se.umu.cs._5dv147_proj.message.type;

import remote.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class ElectionMessage extends AbstractMessage {

    public ElectionMessage(int code) {
        super(code);
    }

    public int getCode() {
        System.err.println("TJA från elaction");
        return super.getCode();
    }

    public void method(int i) {

    }
}
