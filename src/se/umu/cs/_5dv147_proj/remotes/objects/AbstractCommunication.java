package se.umu.cs._5dv147_proj.remotes.objects;

import se.umu.cs._5dv147_proj.remotes.interfaces.ProxyInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by c10mjn on 2015-10-05.
 */
public abstract class AbstractCommunication implements Serializable {

    abstract public void send(AbstractContainer abstractContainer, ArrayList<ProxyInterface> aps);

    abstract public void receive(AbstractContainer message);

}
