package se.umu.cs._5dv147_proj.middleware.message.type;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractMessage;

/**
 * Created by c10mjn on 13/10/15.
 */
public class ErrorMessage extends AbstractMessage{
    private ProxyInterface errorProxy;


    public ErrorMessage(ProxyInterface errorProxy) {
        super();
        this.errorProxy = errorProxy;
    }

    public ProxyInterface getProxy(){
        return this.errorProxy;
    }

}
