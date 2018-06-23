package com.oocl.trello.ticket.controller;

import com.julienvey.trello.domain.*;
import com.julienvey.trello.impl.TrelloImpl;
import com.oocl.trello.ticket.model.*;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindowView;
import javax.swing.*;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class MainWindowController {

    private final Config config;
    private TrelloImpl trelloApi;
    private Board board;
    private MainWindowView mainWindowView;
    private TicketProcessService ticketProcessService;
    private ScheduledExecutorService exec;
    private List<Ticket> cachedPortalTickets;
    private ButtonGroup listRadioGroup;
    private ArrayList<JCheckBox> trelloLabelCheckBoxes;
    private ArrayList<JCheckBox> trelloMembersCheckBoxes;
    private List<Label> trelloLabels;
    private List<Member> trelloMembers;
    private List<TList> trelloLists;

    MainWindowController(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
        config = Util.getConfig();
        this.ticketProcessService = new TicketProcessService();
        if (config != null) {
            trelloApi = new TrelloImpl(config.getKey(), config.getToken());
            board = trelloApi.getBoard(config.getBoard());
        }


        initController();
    }

    private void initController() {
        listRadioGroup = new ButtonGroup();
        trelloLabelCheckBoxes = new ArrayList<>();
        trelloMembersCheckBoxes = new ArrayList<>();

        this.mainWindowView.getRunButton().addActionListener(e -> onRun());
        this.mainWindowView.getStopButton().addActionListener(e -> onStop());
        this.mainWindowView.getGenerateReportButton().addActionListener(e -> onGenerateReportButton());
        this.mainWindowView.getTestButton().addActionListener(e -> onTestButton());

        JPanel labelPanel = this.mainWindowView.getLabelPanel();
        JPanel memberPanel = this.mainWindowView.getMembersPanel();
        JPanel listPanel = this.mainWindowView.getListPanel();

        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
        memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.PAGE_AXIS));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

        trelloLists = board.fetchLists();

        if (trelloLists != null) {
            for (TList trelloList : trelloLists) {
                JRadioButton radio = new JRadioButton(trelloList.getName());
                radio.setSelected(true);
                listRadioGroup.add(radio);
                listPanel.add(radio);
            }

        }
        trelloLabels = trelloApi.getBoardLabels(board.getId());

        if (trelloLabels != null) {
            for (Label trelloLabel : trelloLabels) {
                JCheckBox newCheckBox =new JCheckBox(String.format("%s(%s)", trelloLabel.getName(), trelloLabel.getColor()));
                trelloLabelCheckBoxes.add(newCheckBox);
                labelPanel.add(newCheckBox);
            }
        }
        trelloMembers = trelloApi.getBoardMembers(board.getId());

        if (trelloMembers != null) {
            for (Member trelloMember : trelloMembers) {
                JCheckBox newCheckBox = new JCheckBox(String.format("%s(%s)", trelloMember.getFullName(), trelloMember.getUsername()));
                trelloMembersCheckBoxes.add(newCheckBox);
                memberPanel.add(newCheckBox);
            }
        }


    }

    private void onTestButton() {
        execute();
    }

    private void onGenerateReportButton() {


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

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("\n=========================================\n");
        stringBuffer.append("Description Generation Date: ").append(LocalDate.now()).append("\n");
        stringBuffer.append("Card Created: ").append(createdCardActions.get(0).getDate()).append(" (").append(createDayDifference).append(" days ago)\n");
        for (com.julienvey.trello.domain.Action action : updatedCardAction) {
            LocalDate date = action.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long dayDifference = ChronoUnit.DAYS.between(date, LocalDate.now());
            if(action.getType().equals("updateCard")){
                stringBuffer.append("Moved to ").append(action.getData().getListAfter().getName()).append(" ").append(action.getDate()).append(" (").append(dayDifference).append(" days ago)\n");
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
        Runnable executeRunnable = this::startRun;
        exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(executeRunnable, 0,  Long.parseLong(mainWindowView.getMinutesTextField().getText()), TimeUnit.MINUTES);
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

            List<Card> trelloCards = board.fetchCards();
            List<Card> newTrelloCards = new ArrayList<>();


            ticketLoop:for (Ticket ticket : newTickets) {
                for (Card card : trelloCards) {
                    if (card.getName().contains(ticket.getNumber())) {
                        continue ticketLoop;
                    }
                }

                newTrelloCards.add(createTicketToCard(ticket));
            }

            if (newTrelloCards.size() == 0) {
                System.out.println("No new trello cards");
                return;
            }
            //todo create for execution of test , only 1 card
//            for (Card newTrelloCard : newTrelloCards) {
//                trelloApi.createCard(newTrelloCard.getIdList(), newTrelloCard);
//            }
            //todo comment this , this is just for testing
            for (int i = 0; i <1 ; i++) {
                Card newCard = trelloApi.createCard(newTrelloCards.get(i).getIdList(), newTrelloCards.get(i));
                List<String> labels = newTrelloCards.get(i).getLabels().stream().map(Label::getColor).collect(Collectors.toList());
                String[] arrayLabels = labels.toArray(new String[0]);
                trelloApi.addLabelsToCard(newCard.getId(),arrayLabels);

            }



        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindowView, e, "Error Reading Portal", JOptionPane.ERROR_MESSAGE);
        }

    }
    private int getSelectedListRadioButtonIndex(ButtonGroup buttonGroup) {
        int index = 0;
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return index;
            }
            index++;
        }

        return index;
    }

    private Card createTicketToCard(Ticket ticket) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        Card newCard = new Card();
        newCard.setName(ticket.getNumber() + '-' + ticket.getSubject());
        newCard.setDesc(String.format("Level : %s\nTicket Created Date: %s\n%sCard Created:%s\n", ticket.getSeverity(), ticket.getCreateDate(), ticket.getSubject(), dateFormat.format(date)));

        int selectedRadioListIndex = getSelectedListRadioButtonIndex(listRadioGroup);
        TList selectedList = trelloLists.get(selectedRadioListIndex);
        List<Label> selectedLabel = IntStream.range(0, trelloLabelCheckBoxes.size()).filter(i -> trelloLabelCheckBoxes.get(i).isSelected()).mapToObj(i -> trelloLabels.get(i)).collect(Collectors.toList());
        List<Member> selectedMember = IntStream.range(0, trelloMembersCheckBoxes.size()).filter(i -> trelloMembersCheckBoxes.get(i).isSelected()).mapToObj(i -> trelloMembers.get(i)).collect(Collectors.toList());

        newCard.setIdList(selectedList.getId());
        newCard.setIdMembers(selectedMember.stream().map(Member::getId).collect(Collectors.toList()));
        newCard.setLabels(selectedLabel);


        return newCard;
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
