package se.umu.cs._5dv147_proj.client.gui;


import se.umu.cs._5dv147_proj.client.gui.Contents.FrameListener;
import se.umu.cs._5dv147_proj.client.gui.Contents.GroupListFrame;
import se.umu.cs._5dv147_proj.client.gui.Contents.SettingsFrame;
import se.umu.cs._5dv147_proj.client.gui.Contents.SmartScroller;
import se.umu.cs._5dv147_proj.middleware.Middleware;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class ClientGUI {
    private JFrame frame;
    private DebugGUI debugFrame;
    private JPanel chatPanel;
    private JTextArea chatWindow;
    private JTextField chatMessage;
    private DefaultTableModel userTable;
    private Middleware mw;

    public ClientGUI() {
        this.frame= new JFrame("ChatClient2000");
        this.frame.addWindowListener(new FrameListener(this));
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.chatPanel = new JPanel(new BorderLayout());
        this.frame.add(this.chatPanel);

        buildChatWindow();
        buildChatMessage();

        SettingsFrame sf = new SettingsFrame("localhost", "33401");
        sf.waitUntilDisposed();

        ArrayList<String> args = new ArrayList<>();
        args.add("-a");
        args.add(sf.getNameServerAdress());
        args.add("-p");
        args.add(sf.getNameServerPort());
        args.add("-u");
        args.add(sf.getNickName());
        args.add("-c");
        args.add("causal");
        if(sf.getDebug()){
            args.add("-d");
            this.debugFrame = new DebugGUI();
            this.frame.add(debugFrame, BorderLayout.NORTH);
        }

        mw = new Middleware(args.toArray(new String[1]));

        GroupListFrame glf = new GroupListFrame(mw);

        mw.registerActionListener(e -> {
            Debug.getDebug().log("GUI action listener; " + e.getActionCommand());
            if(e.getActionCommand().equals("TextMessage")){
                chatWindow.append(mw.receive() + "\n");
                debugFrame.updateHoldBackQueueTable();
            }else if(e.getActionCommand().equals("UpdateUsers")){
                setUsers(mw.getNameList());
            }
            frame.repaint();
        });

        glf.waitUntilDisposed();

        this.frame.setVisible(true);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.chatMessage.requestFocus();
    }

    private void buildChatWindow(){
        this.chatWindow = new JTextArea();
        this.chatWindow.setBackground(Color.WHITE);
        this.chatWindow.setEditable(false);

        JScrollPane scroll = new JScrollPane(this.chatWindow);
        scroll.setPreferredSize(new Dimension(800, 600));
        new SmartScroller(scroll);

        this.chatPanel.add(scroll, BorderLayout.CENTER);
    }

    private void buildChatMessage(){
        this.chatMessage = new JTextField();
        this.chatMessage.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    mw.send(chatMessage.getText());
                    chatMessage.setText("");
                }
            }
        });

        this.chatMessage.setPreferredSize(new Dimension(500, 20));
        this.chatPanel.add(this.chatMessage, BorderLayout.SOUTH);
    }

    private void buildUserWindow(){
        String[] header = new String[] {"Name"};

        this.userTable = new DefaultTableModel(header, 0){
            private static final long serialVersionUID = -6564664247927106428L;

            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        JTable userWindow = new JTable(this.userTable);
        userWindow.setAutoCreateRowSorter(true);
        userWindow.setGridColor(Color.GRAY);
        userWindow.setShowGrid(true);
        userWindow.setRowHeight(20);
        userWindow.setBackground(Color.WHITE);

        this.chatPanel.add(userWindow, BorderLayout.EAST);
    }

    private synchronized void setUsers(ArrayList<String> names){
        if(this.userTable != null) {
            for(int i = 0; i < this.userTable.getRowCount(); i++){
                this.userTable.removeRow(i);
            }
            for(int i = 0; i < names.size(); i++){
                this.userTable.addRow(new String[] {names.get(i)});
            }
            this.userTable.fireTableDataChanged();
        }
    }

    public Middleware getMiddleWare() {
        return this.mw;
    }
}