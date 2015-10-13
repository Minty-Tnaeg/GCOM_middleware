package se.umu.cs._5dv147_proj.middleware.communication.api;

import se.umu.cs._5dv147_proj.middleware.communication.module.BasicCommunicationModule;
import se.umu.cs._5dv147_proj.middleware.message.type.TextMessage;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractProxy;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by c12slm on 2015-10-13.
 */
public class DebugProxy extends AbstractProxy {
    private String nickName;
    private BasicCommunicationModule cm;
    private Random rand;
    private ArrayList<AbstractContainer> reOrder;

    public DebugProxy(String nickName) {
        super(nickName);
        this.nickName = nickName;
        this.rand = new Random();
        this.reOrder = new ArrayList<>();
    }

    public void setCommunuicationModule(BasicCommunicationModule cm){
        this.cm = cm;
    }

    @Override
    public void receiveMessage(AbstractContainer ac) throws RemoteException {
        if(ac.getMessage().getClass() == TextMessage.class){
            if(Math.random()*100 < Debug.getDebug().getDropRate()){
                Debug.getDebug().log("DROPPING PACKET");
                return;
            }

            try {
                int bound = Debug.getDebug().getMaxDelay() - Debug.getDebug().getMinDelay();
                if(bound > 1){
                    int time = Debug.getDebug().getMinDelay() + rand.nextInt(bound);
                    Debug.getDebug().log("DELAYING PACKET BY " + time);
                    Thread.sleep(time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Debug.getDebug().shouldReorder()){
                Debug.getDebug().log("REORDERING PACKET");
                reOrder.add(ac);
                if(reOrder.size() >= 4){
                    AbstractContainer send = reOrder.get(rand.nextInt(4));
                    reOrder.remove(send);
                    cm.receive(send);
                }
            }else{
                reOrder.forEach(cm::receive);
                reOrder.clear();
                cm.receive(ac);
            }
        }else{
            cm.receive(ac);
        }

    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    @Override
    public long ping() throws RemoteException {
        return System.currentTimeMillis();
    }
}
