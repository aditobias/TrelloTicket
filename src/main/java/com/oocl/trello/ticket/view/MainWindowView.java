package com.oocl.trello.ticket.view;

import javax.swing.*;

public class MainWindowView extends JFrame {
    private JTabbedPane mainTabbedPanel;
    private JPanel mainPanel;
    private JPanel configPanel;
    private JPanel mainTabbed;
    private JTabbedPane tabbedPane1;
    private JTextArea consoleTextArea;
    private JTable configTable;
    private JTextField minutesTextField;
    private JButton runButton;
    private JButton stopButton;
    private JRadioButton radioButto;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JPanel ListPanel;
    private JPanel membersPanel;
    private JPanel labelPanel;
    private JButton generateReportButton;
    private JButton emailBAFollowUpButton;

    public MainWindowView() {
        setContentPane(mainTabbedPanel);
        setTitle("Trello Ticket by Peter Barredo");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setVisible(true);

    }

    public JTabbedPane getMainTabbedPanel() {
        return mainTabbedPanel;
    }

    public void setMainTabbedPanel(JTabbedPane mainTabbedPanel) {
        this.mainTabbedPanel = mainTabbedPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getConfigPanel() {
        return configPanel;
    }

    public void setConfigPanel(JPanel configPanel) {
        this.configPanel = configPanel;
    }

    public JPanel getMainTabbed() {
        return mainTabbed;
    }

    public void setMainTabbed(JPanel mainTabbed) {
        this.mainTabbed = mainTabbed;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public void setTabbedPane1(JTabbedPane tabbedPane1) {
        this.tabbedPane1 = tabbedPane1;
    }

    public JTextArea getConsoleTextArea() {
        return consoleTextArea;
    }

    public void setConsoleTextArea(JTextArea consoleTextArea) {
        this.consoleTextArea = consoleTextArea;
    }

    public JTable getConfigTable() {
        return configTable;
    }

    public void setConfigTable(JTable configTable) {
        this.configTable = configTable;
    }

    public JTextField getMinutesTextField() {
        return minutesTextField;
    }

    public void setMinutesTextField(JTextField minutesTextField) {
        this.minutesTextField = minutesTextField;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public void setRunButton(JButton runButton) {
        this.runButton = runButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public void setStopButton(JButton stopButton) {
        this.stopButton = stopButton;
    }

    public JRadioButton getRadioButto() {
        return radioButto;
    }

    public void setRadioButto(JRadioButton radioButto) {
        this.radioButto = radioButto;
    }

    public JCheckBox getCheckBox1() {
        return checkBox1;
    }

    public void setCheckBox1(JCheckBox checkBox1) {
        this.checkBox1 = checkBox1;
    }

    public JCheckBox getCheckBox2() {
        return checkBox2;
    }

    public void setCheckBox2(JCheckBox checkBox2) {
        this.checkBox2 = checkBox2;
    }

    public JPanel getListPanel() {
        return ListPanel;
    }

    public void setListPanel(JPanel listPanel) {
        ListPanel = listPanel;
    }

    public JPanel getMembersPanel() {
        return membersPanel;
    }

    public void setMembersPanel(JPanel membersPanel) {
        this.membersPanel = membersPanel;
    }

    public JPanel getLabelPanel() {
        return labelPanel;
    }

    public void setLabelPanel(JPanel labelPanel) {
        this.labelPanel = labelPanel;
    }


}
