package com.oocl.trello.ticket.view;

import javax.swing.*;

public class MainWindow extends JFrame {
    private JTabbedPane mainTabbedPanel;
    private JPanel mainPanel;
    private JPanel memberPanel;
    private JPanel configPanel;
    private JPanel mainTabbed;
    private JTabbedPane tabbedPane1;
    private JTextArea consoleTextArea;
    private JTable configTable;
    private JTextField minutesTextField;
    private JButton runButton;
    private JButton stopButton;

    public MainWindow() {
        setContentPane(mainTabbedPanel);
        setTitle("Trello Ticket by Peter Barredo");
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setVisible(true);
    }

}
