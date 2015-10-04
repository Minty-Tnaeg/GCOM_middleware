package se.umu.cs._5dv147_proj.message.type;

/**
 * Created by c10mjn on 04/10/15.
 */
public class ElectionMessage extends AbstractMessage {


    public ElectionMessage(int code) {
        super(code);
    }

    @Override
    public int getCode() {
        System.err.println("TJA från elaction");
        return super.getCode();
    }

    @Override
    void method(int i) {

    }
}
