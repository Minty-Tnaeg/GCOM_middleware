package se.umu.cs._5dv147_proj.client.gui.Contents;



import se.umu.cs._5dv147_proj.middleware.Middleware;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by c12slm on 2015-10-02.
 */
public class GroupListFrame {
    private JFrame serverFrame;
    private JPanel tablePanel;
    private JTable groupTable;
    private Middleware mw;
    private String[][] groupList;
    private static String[] header = {"Groupname", "Leader"};
    private JTextField newGroup;

    public GroupListFrame(Middleware mw) {
        this.mw = mw;
        this.serverFrame = new JFrame("Available groups");
        this.serverFrame.setResizable(true);
        this.tablePanel = new JPanel(new BorderLayout());
        this.serverFrame.add(this.tablePanel);
        this.tablePanel.setPreferredSize(new Dimension(500, 300));
        this.groupList = mw.getGroupList();
        buildCreateGroupText();
        buildGroupTable();
        buildConnectButton();

        this.serverFrame.setVisible(true);
        this.serverFrame.pack();
        this.serverFrame.setLocationRelativeTo(null);
    }

    private void buildCreateGroupText() {
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(new JLabel("Create new group"), BorderLayout.NORTH);
        this.newGroup = new JTextField();
        this.newGroup.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {}

            @Override
            public void keyPressed(KeyEvent ke) {}

            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ENTER){
                    mw.joinGroup(newGroup.getText());
                    serverFrame.dispose();
                }
            }
        });

        this.newGroup.setPreferredSize(new Dimension(500,20));
        jp.add(this.newGroup, BorderLayout.CENTER);
        jp.add(Box.createRigidArea(new Dimension(0,10)), BorderLayout.SOUTH);
        this.tablePanel.add(jp, BorderLayout.NORTH);
    }

    private void buildGroupTable() {
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(new JLabel("Join group"), BorderLayout.NORTH);

        Object[][] dataTable = groupList;
        DefaultTableModel tableModel = new DefaultTableModel(dataTable, header){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        this.groupTable = new JTable(tableModel);
        this.groupTable.setAutoCreateRowSorter(true);
        this.groupTable.setGridColor(Color.GRAY);
        this.groupTable.setShowGrid(true);
        this.groupTable.setRowHeight(20);
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(this.groupTable);

        jp.add(jsp, BorderLayout.CENTER);
        this.tablePanel.add(jp, BorderLayout.CENTER);
    }

    private void buildConnectButton(){
        JButton button = new JButton("Connect");
        button.addActionListener(ae -> {
            if(groupTable.getSelectedRow() == -1){
                if(!this.newGroup.getText().isEmpty()) {
                    mw.joinGroup(this.newGroup.getText());
                    serverFrame.dispose();
                }
            }else{
                mw.joinGroup(groupList[groupTable.getSelectedRow()][0]);
                serverFrame.dispose();
            }
        });
        this.tablePanel.add(button, BorderLayout.SOUTH);
    }

    public void waitUntilDisposed() {
        while(this.serverFrame.isDisplayable()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}