package com.oocl.trello.ticket;

import com.oocl.trello.ticket.controller.TicketDialogWindowController;
import com.oocl.trello.ticket.view.TicketDialogWindow;
import org.apache.log4j.Logger;

public class App {
    public final static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {

        TicketDialogWindow ticketDialogWindow = new TicketDialogWindow();
        TicketDialogWindowController ticketDialogWindowController = new TicketDialogWindowController(ticketDialogWindow);
        logger.info("=================Trello Ticket Application Started=================");

    }
}
