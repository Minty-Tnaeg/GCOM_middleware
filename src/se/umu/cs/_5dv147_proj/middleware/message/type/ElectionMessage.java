package se.umu.cs._5dv147_proj.middleware.message.type;


import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c10mjn on 04/10/15.
 */
public class ElectionMessage extends AbstractMessage {
    private ProxyInterface proxy;

    public ElectionMessage(ProxyInterface proxy, ProxyInterface sender) {
        super(sender);
        this.proxy = proxy;
    }

    public ProxyInterface getProxy() {
        return proxy;
    }
}
