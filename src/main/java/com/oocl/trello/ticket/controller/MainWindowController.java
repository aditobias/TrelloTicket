package com.oocl.trello.ticket.controller;


import com.oocl.trello.ticket.service.TrelloService;
import com.oocl.trello.ticket.model.Column;
import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.Label;
import com.oocl.trello.ticket.model.User;
import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindowView;

import javax.swing.*;
import java.util.List;


public class MainWindowController {

    private MainWindowView mainWindowView;
    private TrelloService trelloService;

    public MainWindowController(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
        Config config = Util.getConfig();
        this.trelloService = new TrelloService(config);
        initController();
    }

    private void initController() {
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


}
