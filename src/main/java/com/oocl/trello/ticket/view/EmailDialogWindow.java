package com.oocl.trello.ticket.view;

import com.julienvey.trello.domain.Board;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmailDialogWindow extends JDialog {
    private JPanel contentPane;

    private JButton buttonSend;
    private JButton buttonCancel;
    private JTextField recipientsJTextField;
    private JTextArea emailContentJTextArea;

    public EmailDialogWindow() {
        setContentPane(contentPane);
        pack();
        getRootPane().setDefaultButton(buttonSend);
        setSize(650,600);
        setVisible(true);
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public JButton getButtonSend() {
        return buttonSend;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public JTextArea getEmailContentPanel() {
        return emailContentJTextArea;
    }

    public JTextField getRecipientsJTextField() {
        return recipientsJTextField;
    }
}
