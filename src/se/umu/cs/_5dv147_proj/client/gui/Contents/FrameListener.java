package se.umu.cs._5dv147_proj.client.gui.Contents;

import se.umu.cs._5dv147_proj.client.gui.ClientGUI;

import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

/**
 * Created by c10mjn on 2015-10-08.
 */
public class FrameListener implements java.awt.event.WindowListener {
    public ClientGUI clientWindow;

    public FrameListener(ClientGUI window) {
        this.clientWindow = window;

    }


    @Override
    public void windowOpened(WindowEvent e) {
        //Do nothing.

    }

    @Override
    public void windowClosing(WindowEvent e) {
        /*try {
            clientWindow.getMiddleWare().sendLeave();
        } catch (RemoteException e1) {
            System.err.println("Remote exception:" + e1.getMessage());
        }*/
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
