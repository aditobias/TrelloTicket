package com.oocl.trello.ticket;

import com.oocl.trello.ticket.controller.TicketDialogWindowController;
import com.oocl.trello.ticket.view.TicketDialogWindow;
import org.apache.log4j.Logger;

public class App {
    private final static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {

        new TicketDialogWindowController(new TicketDialogWindow());

//        MainWindowController mainWindowController = new MainWindowController(new MainWindowView());
        logger.info("=================Trello Ticket Application Started=================");

    }
}
