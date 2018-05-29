package com.oocl.trello.ticket.controller;

import com.intellij.uiDesigner.core.GridConstraints;
import com.oocl.trello.ticket.view.MainWindowView;

import javax.swing.*;
import java.awt.*;

public class MainWindowController {

    MainWindowView mainWindowView;
    public MainWindowController(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
        initController();
    }

    private void initController() {
        JCheckBox checkBox3 = new JCheckBox("testasdasd");
        JCheckBox checkBox1 = new JCheckBox("testasdasd");
        JCheckBox checkBox2 = new JCheckBox("testasdasd");
        JCheckBox checkBox4 = new JCheckBox("testasdasd");

        JPanel labelPanel = this.mainWindowView.getLabelPanel();
//        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
        labelPanel.add(checkBox1);
//        labelPanel.add(checkBox2);
//        labelPanel.add(checkBox3);
//        labelPanel.add(checkBox4);

    }

}
