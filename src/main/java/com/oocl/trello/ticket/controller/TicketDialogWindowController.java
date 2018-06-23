package com.oocl.trello.ticket.controller;

import com.oocl.trello.ticket.model.Ticket;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.view.MainWindowView;
import com.oocl.trello.ticket.view.TicketDialogWindow;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TicketDialogWindowController {

    private TicketDialogWindow ticketDialogWindow;
    private TicketProcessService ticketProcessService;

    public TicketDialogWindowController(TicketDialogWindow ticketDialogWindow) {
        this.ticketDialogWindow = ticketDialogWindow;
        this.ticketProcessService = new TicketProcessService();
        initController();
    }

    private void initController() {
        this.ticketDialogWindow.getButtonOK().addActionListener(e -> onOK());

        this.ticketDialogWindow.getButtonCancel().addActionListener(e -> onCancel());

        this.ticketDialogWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        this.ticketDialogWindow.getContentPane().registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        Ticket ticket = null;
        try {
            ticket = this.ticketProcessService.getTicketFromPortal().get(0);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ticketDialogWindow, e, "Error Reading Portal", JOptionPane.ERROR_MESSAGE);
        }


        this.ticketDialogWindow.getNumberTextField().setText(ticket != null ? ticket.getNumber() : null);
        this.ticketDialogWindow.getCategoryTextField().setText(ticket != null ? ticket.getCategory() : null);
        this.ticketDialogWindow.getLinkTextField().setText(ticket != null ? ticket.getLink() : null);
        this.ticketDialogWindow.getSeverityTextField().setText(ticket != null ? ticket.getSeverity() : null);
        this.ticketDialogWindow.getSubjectTextField().setText(ticket != null ? ticket.getSubject() : null);
        this.ticketDialogWindow.getStatusTextField().setText(ticket != null ? ticket.getStatus() : null);
        this.ticketDialogWindow.getResponsiblePartyTextField().setText(ticket != null ? ticket.getResponseParty() : null);
        this.ticketDialogWindow.getCreationDateTextField().setText(ticket != null ? ticket.getCreateDate() : null);
    }

    private void onOK() {
        new MainWindowController(new MainWindowView());
        this.ticketDialogWindow.dispose();
    }

    private void onCancel() {
        this.ticketDialogWindow.dispose();
    }
}
