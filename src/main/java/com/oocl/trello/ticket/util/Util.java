package com.oocl.trello.ticket.util;

import com.oocl.trello.ticket.model.Config;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    private final static Logger logger = Logger.getLogger(Util.class);
    private static Config config = new Config();

    public static Config getConfig() {
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            config.setKey((String) prop.get("KEY"));
            config.setToken((String) prop.get("TOKEN"));
            config.setBoard((String) prop.get("BOARD"));
            config.setUrl((String) prop.get("URL"));

            return config;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }


        return null;
    }


}
