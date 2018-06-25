package com.oocl.trello.ticket.controller;

import com.julienvey.trello.domain.*;
import com.julienvey.trello.impl.TrelloImpl;
import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.TCard;
import com.oocl.trello.ticket.model.TLabel;
import com.oocl.trello.ticket.model.Ticket;
import com.oocl.trello.ticket.service.ExcelService;
import com.oocl.trello.ticket.service.TicketProcessService;
import com.oocl.trello.ticket.service.TrelloService;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.EmailDialogWindow;
import com.oocl.trello.ticket.view.MainWindowView;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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


public class MainWindowController {

    private Config config;
    private TrelloImpl trelloApi;
    private Board board;
    private MainWindowView mainWindowView;
    private TicketProcessService ticketProcessService;
    private ScheduledExecutorService exec;
    private List<Ticket> cachedPortalTickets;
    private ButtonGroup listRadioGroup;
    private ArrayList<JCheckBox> trelloLabelCheckBoxes;
    private ArrayList<JCheckBox> trelloMembersCheckBoxes;
    private List<TLabel> trelloLabels;
    private List<Member> trelloMembers;
    private List<TList> trelloLists;
    private TrelloService trelloService;

    public MainWindowController(MainWindowView mainWindowView) {

        try {
            this.mainWindowView = mainWindowView;

            config = Util.getConfig();
            this.ticketProcessService = new TicketProcessService(config);
            if (config != null) {
                trelloApi = new TrelloImpl(config.getKey(), config.getToken());
                trelloService = new TrelloService(config.getKey(), config.getToken());
                board = trelloApi.getBoard(config.getBoard());
                this.mainWindowView.setTitle("Trello Ticket : " + board.getName() + " Board");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindowView, e, "Trello Authorization Error!", JOptionPane.ERROR_MESSAGE);
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
        this.mainWindowView.getEmailBAFollowUpButton().addActionListener(e -> onSendEmail());
        this.mainWindowView.getUpdateDescriptionButton().addActionListener(e -> onUpdateCardDescription());

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
        trelloLabels = trelloService.getBoardTLabels(board.getId());

        if (trelloLabels != null) {
            for (Label trelloLabel : trelloLabels) {
                JCheckBox newCheckBox = new JCheckBox(String.format("%s(%s)", trelloLabel.getName(), trelloLabel.getColor()));
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
    private void onSendEmail() {
        new EmailDialogController(new EmailDialogWindow(), board);
        //EmailService.sendEmail();
    }

    private void onGenerateReportButton() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isDirectory()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());

                ExcelService.FILE_NAME = jfc.getSelectedFile().toString() + "/MyFirstExcel.xlsx";
                ExcelService.generateExcel();
            }
        }

    }

    private void onTestButton() {
        cachedPortalTickets = null;
        execute(true);

    }

    private void onUpdateCardDescription() {


        List<com.julienvey.trello.domain.Card> cards = board.fetchCards();


        for (com.julienvey.trello.domain.Card card : cards) {

            List<com.julienvey.trello.domain.Action> createdCardActions = trelloApi.getCardActions(card.getId(), new Argument("filter", "createCard"));
            List<com.julienvey.trello.domain.Action> updateCardActions = card.getActions();

            appendCardDescription(card, createdCardActions, updateCardActions);

            //todo create custom trello service for update card description only
//            card.update();


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
            if (action.getType().equals("updateCard")) {
                stringBuffer.append("Moved to ").append(action.getData().getListAfter().getName()).append(" ").append(action.getDate()).append(" (").append(dayDifference).append(" days ago)\n");
            }
        }
        stringBuffer.append("\n=========================================\n");
        String currentDescription = card.getDesc();
        int first = currentDescription.indexOf("\n=");
        int last = currentDescription.lastIndexOf("=\n") + 2;

        if (first != -1 && last != -1) {
            String toBeReplaced = currentDescription.substring(first, last);
            card.setDesc(currentDescription.replace(toBeReplaced, "") + "\n" + stringBuffer.toString());
        } else {
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
        exec.scheduleAtFixedRate(executeRunnable, 0, Long.parseLong(mainWindowView.getMinutesTextField().getText()), TimeUnit.MINUTES);
        mainWindowView.getRunButton().setEnabled(false);
        mainWindowView.getStopButton().setEnabled(true);
    }

    private void startRun() {
        JTextArea consoleTextArea = mainWindowView.getConsoleTextArea();

        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to run it in "+ board.getName()+" board?","Warning",JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            onRunDisplay(consoleTextArea);
            execute(false);
        }

        consoleTextArea.setText(consoleTextArea.getText() + "Done! \n");

    }

    private void execute(boolean isTest) {

        List<Ticket> portalTickets;
        List<Ticket> newTickets = new ArrayList<>();


        try {
            portalTickets = ticketProcessService.getTicketFromPortal();
            if (cachedPortalTickets == null) {
                newTickets = portalTickets;
            } else {
                loop1:
                for (Ticket portalTicket : portalTickets) {
                    for (Ticket cachedPortalTicket : cachedPortalTickets) {
                        if (portalTicket.getNumber().equals(cachedPortalTicket.getNumber())) {
                            continue loop1;
                        }
                    }
                    newTickets.add(portalTicket);
                }
            }

            cachedPortalTickets = portalTickets;

            if (newTickets.size() == 0) {
                System.out.println("No new ticket in portal");
                return;
            }

            List<Card> trelloCards = board.fetchCards();
            List<Card> newTrelloCards = new ArrayList<>();


            ticketLoop:
            for (Ticket ticket : newTickets) {
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

            if (isTest) {
                testCreateCard(newTrelloCards);
            } else {
                createAllNewCardToTrello(newTrelloCards);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindowView, e, "Error Reading Portal", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void createAllNewCardToTrello(List<Card> newTrelloCards) {
        for (Card newTrelloCard : newTrelloCards) {
            Card newCard = trelloApi.createCard(newTrelloCard.getIdList(), newTrelloCard);
            String[] arrayLabels = newTrelloCard.getLabels().stream().map(Label::getColor).toArray(String[]::new);
            trelloApi.addLabelsToCard(newCard.getId(), arrayLabels);
            trelloApi.addUrlAttachmentToCard(newCard.getId(), newTrelloCards.get(0).getUrl());
        }
    }

    private void testCreateCard(List<Card> newTrelloCards) {
        Card newCard = trelloApi.createCard(newTrelloCards.get(0).getIdList(), newTrelloCards.get(0));
        String[] arrayLabels = newTrelloCards.get(0).getLabels().stream().map(Label::getColor).toArray(String[]::new);
        trelloApi.addLabelsToCard(newCard.getId(), arrayLabels);
        trelloApi.addUrlAttachmentToCard(newCard.getId(), newTrelloCards.get(0).getUrl());
    }

    private int getSelectedListRadioButtonIndex(ButtonGroup buttonGroup) {
        int index = 0;
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
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
        newCard.setDesc(String.format("Level : %s\nTicket Created Date: %s\n%s\nCard Created:%s\n", ticket.getSeverity(), ticket.getCreateDate(), ticket.getSubject(), dateFormat.format(date)));

        int selectedRadioListIndex = getSelectedListRadioButtonIndex(listRadioGroup);
        TList selectedList = trelloLists.get(selectedRadioListIndex);
        List<Label> selectedLabel = IntStream.range(0, trelloLabelCheckBoxes.size()).filter(i -> trelloLabelCheckBoxes.get(i).isSelected()).mapToObj(i -> trelloLabels.get(i)).collect(Collectors.toList());
        List<Member> selectedMember = IntStream.range(0, trelloMembersCheckBoxes.size()).filter(i -> trelloMembersCheckBoxes.get(i).isSelected()).mapToObj(i -> trelloMembers.get(i)).collect(Collectors.toList());

        newCard.setIdList(selectedList.getId());
        newCard.setIdMembers(selectedMember.stream().map(Member::getId).collect(Collectors.toList()));
        newCard.setLabels(selectedLabel);
        newCard.setUrl(ticket.getLink());

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
