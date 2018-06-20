package com.oocl.trello.ticket.controller;


import com.oocl.trello.ticket.model.*;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.service.TrelloService;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class MainWindowController {

    private final Config config;
    private MainWindowView mainWindowView;
    private TrelloService trelloService;
    private TicketProcessService ticketProcessService;
    private Runnable executeRunnable;
    private ScheduledExecutorService exec;
    private List<Ticket> cachedPortalTickets;

    public MainWindowController(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
        config = Util.getConfig();
        this.trelloService = new TrelloService(config);
        this.ticketProcessService = new TicketProcessService(config);
        initController();
    }

    private void initController() {
        this.mainWindowView.getRunButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRun();
            }
        });
        this.mainWindowView.getStopButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onStop();
            }
        });

        List<Label> trelloLabels = this.trelloService.getTrelloBoardLabels();
        List<User> trelloUsers = this.trelloService.getTrelloBoardMembers();
        List<Column> trelloColumns = this.trelloService.getTrelloColumns();

        JPanel labelPanel = this.mainWindowView.getLabelPanel();
        JPanel memberPanel = this.mainWindowView.getMembersPanel();
        JPanel listPanel = this.mainWindowView.getListPanel();

        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.PAGE_AXIS));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

        if (trelloColumns != null) {
            ButtonGroup buttonGroup = new ButtonGroup();

            for (Column trelloColumn : trelloColumns) {
                JRadioButton radio = new JRadioButton(trelloColumn.getName());
                buttonGroup.add(radio);
                listPanel.add(radio);
            }
        }

        if (trelloLabels != null) {
            for (Label trelloLabel : trelloLabels) {
                labelPanel.add(new JCheckBox(trelloLabel.getDisplayName()));
            }
        }

        if (trelloUsers != null) {
            for (User trelloUser : trelloUsers) {
                memberPanel.add(new JCheckBox(trelloUser.getDisplayName()));
            }
        }


    }

    private void onStop() {
        exec.shutdown();
        JTextArea consoleTextArea = mainWindowView.getConsoleTextArea();
        consoleTextArea.setText(consoleTextArea.getText() + "Tool already SHUTDOWN" + "\n" );
        mainWindowView.getRunButton().setEnabled(true);
        mainWindowView.getStopButton().setEnabled(false);
    }


    private void onRun() {
        executeRunnable = new Runnable() {
            public void run() {
                startRun();
            }
        };
        exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(executeRunnable , 0, 5, TimeUnit.MINUTES);
        mainWindowView.getRunButton().setEnabled(false);
        mainWindowView.getStopButton().setEnabled(true);
    }

    private void startRun() {
        JTextArea consoleTextArea = mainWindowView.getConsoleTextArea();
        onRunDisplay(consoleTextArea);
        execute();
        consoleTextArea.setText(consoleTextArea.getText() + "Done! \n");

    }

    private void execute() {
        //get All tickets
        List<Ticket> portalTickets;
        List<Ticket> newTicket = new ArrayList<>();

        try {
            portalTickets = ticketProcessService.getTicketFromPortal();

            //check cache ticket

            if(cachedPortalTickets != null){
                //compare cacheportal ticket if there is a new ticket
                loop1:for (Ticket portalTicket : portalTickets) {
                    for (Ticket cachedPortalTicket : cachedPortalTickets) {
                        if (portalTicket.getNumber().equals(cachedPortalTicket.getNumber())) {
                            //continue outer loop
                            continue loop1;

                        }
                    }
                }
            }

            cachedPortalTickets = portalTickets;





        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindowView,e,"Error Reading Portal",JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean isTicketInCacheTicket(Ticket ticket) {
        return cachedPortalTickets.stream().filter(ticketObject -> ticketObject.getNumber() == ticket.getNumber()).count() == 0;
    }


    private void onRunDisplay(JTextArea consoleTextArea) {
        consoleTextArea.setText(consoleTextArea.getText() + "================" + "\n" );
        consoleTextArea.setText(consoleTextArea.getText() + "KEY : " + config.getKey() + "\n" );
        consoleTextArea.setText(consoleTextArea.getText() + "TOKEN : " + config.getToken() + "\n" );
        consoleTextArea.setText(consoleTextArea.getText() + "URL : " + config.getUrl() + "\n" );
        consoleTextArea.setText(consoleTextArea.getText() + "BOARD : " + config.getBoard() + "\n" );
        consoleTextArea.setText(consoleTextArea.getText() + "================" + "\n" );

        consoleTextArea.setText(consoleTextArea.getText() + "Running now! \n");
    }


}
