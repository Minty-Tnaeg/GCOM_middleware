package se.umu.cs._5dv147_proj.remotes.objects;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;

import java.io.Serializable;

/**
 * Created by c10mjn on 04/10/15.
 */
public abstract class AbstractMessage implements Serializable {
    private final ProxyInterface sender;

    public AbstractMessage(ProxyInterface sender) {
        this.sender = sender;
    }

    public ProxyInterface getSender() {
        return sender;
    }
}
