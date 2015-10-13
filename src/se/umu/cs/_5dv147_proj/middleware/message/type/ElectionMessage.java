package se.umu.cs._5dv147_proj.middleware.message.type;


import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

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


}
