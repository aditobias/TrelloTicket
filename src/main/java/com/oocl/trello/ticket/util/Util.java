package com.oocl.trello.ticket.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    public void isCredentialOkay() {

    }
    public void readConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            System.out.println(prop.get("KEY"));
            System.out.println(prop.get("URL"));
            System.out.println(prop.get("BOARD"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
