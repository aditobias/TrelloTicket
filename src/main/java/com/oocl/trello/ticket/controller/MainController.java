package com.oocl.trello.ticket.controller;

import com.oocl.trello.ticket.util.Util;
import com.oocl.trello.ticket.view.MainWindow;

public class MainController {

    public static void main(String[] args) {
//        MainWindow mainWindow = new MainWindow();
        Util util = new Util();
        util.readConfig();

    }

}
