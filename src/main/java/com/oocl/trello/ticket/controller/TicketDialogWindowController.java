package com.oocl.trello.ticket.controller;

import com.oocl.trello.ticket.view.MainWindowView;
import com.oocl.trello.ticket.view.TicketDialogWindow;

import javax.swing.*;
import java.awt.event.*;

public class TicketDialogWindowController {

    TicketDialogWindow ticketDialogWindow;

    public TicketDialogWindowController(TicketDialogWindow ticketDialogWindow) {
        this.ticketDialogWindow = ticketDialogWindow;
        initController();
    }

    private void initController() {
        this.ticketDialogWindow.getButtonOK().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        this.ticketDialogWindow.getButtonCancel().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked

        this.ticketDialogWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        this.ticketDialogWindow.getContentPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        MainWindowView mainWindowView = new MainWindowView();
        MainWindowController mainWindowController = new MainWindowController(mainWindowView);
        this.ticketDialogWindow.dispose();
    }

    private void onCancel() {
        this.ticketDialogWindow.dispose();
    }
}
