package com.oocl.trello.ticket.controller;


import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.impl.TrelloImpl;
import com.oocl.trello.ticket.model.*;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.service.TrelloService;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindowView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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

        this.mainWindowView.getGenerateReportButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGenerateReportButton();
            }
        });

//        List<Label> trelloLabels = this.trelloService.getTrelloBoardLabels();
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

//        if (trelloLabels != null) {
//            for (Label trelloLabel : trelloLabels) {
//                labelPanel.add(new JCheckBox(trelloLabel.getDisplayName()));
//            }
//        }

        if (trelloUsers != null) {
            for (User trelloUser : trelloUsers) {
                memberPanel.add(new JCheckBox(trelloUser.getDisplayName()));
            }
        }


    }

    private void onGenerateReportButton() {
        Trello trelloApi = new TrelloImpl(config.getKey(), config.getToken());
        Board board = trelloApi.getBoard(config.getBoard());
        List<com.julienvey.trello.domain.Card> cards = board.fetchCards();


        for (com.julienvey.trello.domain.Card card : cards) {

            List<com.julienvey.trello.domain.Action> createdCardActions = trelloApi.getCardActions(card.getId(),new Argument("filter","createCard"));
            List<com.julienvey.trello.domain.Action> updateCardActions = card.getActions();

            appendCardDescription(card, createdCardActions, updateCardActions);

            card.update();
        }
    }

    private void appendCardDescription(com.julienvey.trello.domain.Card card, List<com.julienvey.trello.domain.Action> createdCardActions, List<com.julienvey.trello.domain.Action> updatedCardAction) {
        LocalDate createDate = createdCardActions.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long createDayDifference = ChronoUnit.DAYS.between(createDate, LocalDate.now());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n=========================================\n");
        stringBuffer.append("Description Generation Date: " + LocalDate.now() +"\n");
        stringBuffer.append("Card Created: " + createdCardActions.get(0).getDate() +" (" + createDayDifference + " days ago)\n");
        for (com.julienvey.trello.domain.Action action : updatedCardAction) {
            LocalDate date = action.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long dayDifference = ChronoUnit.DAYS.between(date, LocalDate.now());
            if(action.getType().equals("updateCard")){
                stringBuffer.append("Moved to " + action.getData().getListAfter().getName() + " "+ action.getDate()+ " (" + dayDifference + " days ago)\n");
            }

        }
        stringBuffer.append("\n=========================================\n");
        String currentDescription = card.getDesc();
        int first = currentDescription.indexOf("\n=") ;
        int last = currentDescription.lastIndexOf("=\n") +2;

        if(first != -1 && last != -1){
            String toBeReplaced = currentDescription.substring(first, last);
            card.setDesc(currentDescription.replace(toBeReplaced,"") + "\n" + stringBuffer.toString());
        }else{
            card.setDesc(card.getDesc() + "\n" + stringBuffer.toString());

        }
    }

    private void onStop() {
        exec.shutdown();
        JTextArea consoleTextArea = mainWindowView.getConsoleTextArea();
        consoleTextArea.setText(consoleTextArea.getText() + "Tool already SHUTDOWN" + "\n");
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
        exec.scheduleAtFixedRate(executeRunnable, 0, 5, TimeUnit.MINUTES);
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
        List<Ticket> newTickets = new ArrayList<>();


        try {
            portalTickets = ticketProcessService.getTicketFromPortal();
            //check cache ticket
            if (cachedPortalTickets != null) {
                //compare cacheportal ticket if there is a new ticket
                loop1:
                for (Ticket portalTicket : portalTickets) {
                    for (Ticket cachedPortalTicket : cachedPortalTickets) {
                        if (portalTicket.getNumber().equals(cachedPortalTicket.getNumber())) {
                            //continue outer loop
                            continue loop1;
                        }
                    }
                    newTickets.add(portalTicket);

                }
            } else {
                newTickets = portalTickets;
            }

            cachedPortalTickets = portalTickets;

            if (newTickets.size() == 0) {
                System.out.println("No new ticket in portal");
                return;
            }

            List<Card> trelloCards = trelloService.getTrelloCards();
            List<Card> newTrelloCards = new ArrayList<>();

            ticketLoop:
            for (Ticket ticket : newTickets) {
                for (Card card : trelloCards) {
                    if (card.getName().indexOf(ticket.getNumber()) != -1) {
                        continue ticketLoop;
                    }
                }

                newTrelloCards.add(createCard(ticket));
            }

            if (newTrelloCards == null) {
                System.out.println("No new trello cards");
                return;
            }

            newTrelloCards.forEach(newTrelloCard -> System.out.println("Create " + newTrelloCard.getName()));
            trelloService.createCardInTrello(newTrelloCards);


        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindowView, e, "Error Reading Portal", JOptionPane.ERROR_MESSAGE);
        }

    }

    private Card createCard(Ticket ticket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        Card newCard = new Card();
        newCard.setName(ticket.getNumber() + '-' + ticket.getSubject());
        newCard.setDesc(String.format("Level : %s\nTicket Created Date: %s\n%sCard Created:%s\n", ticket.getSeverity(), ticket.getCreateDate(), ticket.getSubject(), dateFormat.format(date)));
        return newCard;
    }

    private boolean isTicketInCacheTicket(Ticket ticket) {
        return cachedPortalTickets.stream().filter(ticketObject -> ticketObject.getNumber() == ticket.getNumber()).count() == 0;
    }


    private void onRunDisplay(JTextArea consoleTextArea) {
        consoleTextArea.setText(consoleTextArea.getText() + "================" + "\n");
        consoleTextArea.setText(consoleTextArea.getText() + "KEY : " + config.getKey() + "\n");
        consoleTextArea.setText(consoleTextArea.getText() + "TOKEN : " + config.getToken() + "\n");
        consoleTextArea.setText(consoleTextArea.getText() + "URL : " + config.getUrl() + "\n");
        consoleTextArea.setText(consoleTextArea.getText() + "BOARD : " + config.getBoard() + "\n");
        consoleTextArea.setText(consoleTextArea.getText() + "================" + "\n");

        consoleTextArea.setText(consoleTextArea.getText() + "Running now! \n");
        System.out.println("test");
    }


}
