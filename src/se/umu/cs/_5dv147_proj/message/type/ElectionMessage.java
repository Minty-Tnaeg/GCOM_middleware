package se.umu.cs._5dv147_proj.message.type;

import remote.interfaces.ComModuleInterface;
import remote.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class ElectionMessage extends AbstractMessage {
    private int code;

    public ElectionMessage(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        System.err.println("TJA från elaction");
        return code;
    }

    public void method() {

    }

    @Override
    public int compareTo(ComModuleInterface o) {
        return 0;
    }
}
