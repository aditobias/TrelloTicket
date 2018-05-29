package com.oocl.trello.ticket.util;

import com.oocl.trello.ticket.model.Config;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    public final static Logger logger = Logger.getLogger(Util.class);

    public void isCredentialOkay() {

    }
    public void readConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            Config config = new Config();
            config.setKey((String) prop.get("KEY"));
            config.setKey((String) prop.get("TOKEN"));
            config.setKey((String) prop.get("BOARD"));
            config.setKey((String) prop.get("URL"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }


    }
}
