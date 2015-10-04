package se.umu.cs._5dv147_proj.message.type;

/**
 * Created by c10mjn on 04/10/15.
 */
public abstract class AbstractMessage {
    private int code;

    public AbstractMessage(int code) { this.code = code; }

    public int getCode() {
        return this.code;
    }

    abstract void method(int i);
}
