package se.umu.cs._5dv147_proj.client.gui;

import se.umu.cs._5dv147_proj.client.gui.Contents.SpringUtilities;
import se.umu.cs._5dv147_proj.middleware.settings.Debug;
import se.umu.cs._5dv147_proj.remotes.objects.AbstractContainer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.Timer;

/**
 * Created by c12slm on 2015-10-07.
 */
public class DebugGUI extends JPanel{
    private JTextField minDelay;
    private JTextField maxDelay;
    private JTextField dropRate;
    private JCheckBox reOrder;
    private JButton setSettings;
    private DefaultTableModel holdBackTable;

    public DebugGUI() {
        super(new BorderLayout());
        buildButtonPanel();
        buildHoldBackQueueTable();

    }

    private void buildButtonPanel()  {
        JPanel buttonPanel = new JPanel(new SpringLayout());

        this.minDelay = new JTextField("0", 5);
        this.maxDelay = new JTextField("0", 5);
        this.dropRate = new JTextField("0", 5);
        this.reOrder = new JCheckBox("", true);

        this.setSettings = new JButton("Set");

        JLabel minLabel = new JLabel("Min delay: ", JLabel.TRAILING);
        JLabel maxLabel = new JLabel("Max delay: ", JLabel.TRAILING);
        JLabel dropLabel = new JLabel("Droprate: ", JLabel.TRAILING);
        JLabel rearrLabel = new JLabel("Rearrange: ", JLabel.TRAILING);
        JLabel setLabel = new JLabel("", JLabel.TRAILING);

        //Min
        buttonPanel.add(minLabel);
        minLabel.setLabelFor(this.minDelay);
        buttonPanel.add(this.minDelay);

        //Max
        buttonPanel.add(maxLabel);
        maxLabel.setLabelFor(this.maxDelay);
        buttonPanel.add(this.maxDelay);

        //Drop
        buttonPanel.add(dropLabel);
        dropLabel.setLabelFor(this.dropRate);
        buttonPanel.add(this.dropRate);

        //ReArrange
        buttonPanel.add(rearrLabel);
        rearrLabel.setLabelFor(this.reOrder);
        buttonPanel.add(this.reOrder);

        //Set button
        buttonPanel.add(setLabel);
        setLabel.setLabelFor(this.setSettings);
        buttonPanel.add(this.setSettings);

        this.setSettings.addActionListener(e -> {
            Debug.getDebug().setDelay(Integer.parseInt(minDelay.getText()), Integer.parseInt(maxDelay.getText()));
            Debug.getDebug().setDropRate(Integer.parseInt(dropRate.getText()));
            Debug.getDebug().setReorder(reOrder.isSelected());
        });

        SpringUtilities.makeCompactGrid(buttonPanel, 1, 10, 5, 5, 5, 5);

        this.add(buttonPanel, BorderLayout.NORTH);

        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateHoldBackQueueTable();
            }
        }, 500, 500);
    }

    private void buildHoldBackQueueTable() {
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(new JLabel("Holdback Queue"), BorderLayout.NORTH);

        String[][] dataTable = new String[][]{{"", ""}};

        String[] header = new String[]{"Message", "Vector Clock"};

        this.holdBackTable = new DefaultTableModel(dataTable, header){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        JTable jt = new JTable(this.holdBackTable);
        jt.setAutoCreateRowSorter(true);
        jt.setGridColor(Color.GRAY);
        jt.setShowGrid(true);
        jt.setRowHeight(20);
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(jt);

        jp.add(jsp, BorderLayout.CENTER);
        this.add(jp, BorderLayout.CENTER);
    }

    public synchronized void updateHoldBackQueueTable(){
        ArrayList<AbstractContainer> holdBackQueue = new ArrayList<>(Debug.getDebug().fetchHoldBackQueue());

        for(int i = this.holdBackTable.getRowCount() - 1; i >= 0; i--) {
            this.holdBackTable.removeRow(i);
        }

        String s = "[";

        for (Map.Entry<UUID, Integer> entry : Debug.getDebug().getCompareVectorClock().entrySet()) {
            s += "(" + Debug.getDebug().convertPid(entry.getKey()) + "," + entry.getValue() + ") , ";
        }

        s = s.substring(0, s.length()-3) +  "]";


        this.holdBackTable.addRow(new String[] {"COMPARE VECTOR CLOCK", s});
        for (AbstractContainer container : holdBackQueue) {
            this.holdBackTable.addRow(new String[]{container.getMessage().toString(), container.toString()});
        }
        this.holdBackTable.fireTableDataChanged();
    }

}
