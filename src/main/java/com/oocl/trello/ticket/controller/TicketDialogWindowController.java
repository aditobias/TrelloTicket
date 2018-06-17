package com.oocl.trello.ticket.controller;

import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.Ticket;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindowView;
import com.oocl.trello.ticket.view.TicketDialogWindow;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class TicketDialogWindowController {

    TicketDialogWindow ticketDialogWindow;
    TicketProcessService ticketProcessService;

    public TicketDialogWindowController(TicketDialogWindow ticketDialogWindow) {
        this.ticketDialogWindow = ticketDialogWindow;
        Config config = Util.getConfig();
        this.ticketProcessService = new TicketProcessService(config);
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

        this.ticketDialogWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        this.ticketDialogWindow.getContentPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        Ticket ticket = null;
        try {
            ticket = this.ticketProcessService.getTicketFromPortal().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ticketDialogWindow,e,"Error Reading Portal",JOptionPane.ERROR_MESSAGE);
        }

        this.ticketDialogWindow.getNumberTextField().setText(ticket.getNumber());
        this.ticketDialogWindow.getCategoryTextField().setText(ticket.getCategory());
        this.ticketDialogWindow.getLinkTextField().setText(ticket.getLink());
        this.ticketDialogWindow.getSeverityTextField().setText(ticket.getSeverity());
        this.ticketDialogWindow.getSubjectTextField().setText(ticket.getSubject());
        this.ticketDialogWindow.getStatusTextField().setText(ticket.getStatus());
        this.ticketDialogWindow.getResponsiblePartyTextField().setText(ticket.getResponseParty());
        this.ticketDialogWindow.getCreationDateTextField().setText(ticket.getCreateDate());

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
