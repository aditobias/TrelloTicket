package com.oocl.trello.ticket.view;

import javax.swing.*;

public class MainWindowView extends JFrame {
    private JTabbedPane mainTabbedPanel;
    private JPanel mainPanel;
    private JPanel configPanel;
    private JPanel mainTabbed;
    private JTabbedPane tabbedPane1;
    private JTextArea consoleTextArea;
    private JTextField minutesTextField;
    private JButton runButton;
    private JButton stopButton;
    private JPanel ListPanel;
    private JPanel membersPanel;
    private JPanel labelPanel;
    private JButton generateReportButton;
    private JButton emailBAFollowUpButton;

    public JTabbedPane getMainTabbedPanel() {
        return mainTabbedPanel;
    }

    public JPanel getMainTabbed() {
        return mainTabbed;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JButton getEmailBAFollowUpButton() {
        return emailBAFollowUpButton;
    }



    public JButton getTestButton() {
        return testButton;
    }

    private JButton testButton;

    public MainWindowView() {
        setContentPane(mainTabbedPanel);
        setTitle("Trello Ticket");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600,400);
        setVisible(true);

    }

    public JButton getGenerateReportButton() {
        return generateReportButton;
    }

    public JTextArea getConsoleTextArea() {
        return consoleTextArea;
    }

    public JTextField getMinutesTextField() {
        return minutesTextField;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JPanel getListPanel() {
        return ListPanel;
    }



    public JPanel getMembersPanel() {
        return membersPanel;
    }



    public JPanel getLabelPanel() {
        return labelPanel;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getConfigPanel() {
        return configPanel;
    }
}
