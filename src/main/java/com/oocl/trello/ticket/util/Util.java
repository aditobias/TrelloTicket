package com.oocl.trello.ticket.util;

import com.google.gson.Gson;
import com.oocl.trello.ticket.model.Column;
import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.Label;
import com.oocl.trello.ticket.model.User;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Util {
    private final static Logger logger = Logger.getLogger(Util.class);
    private static Config config = new Config();

    public void isCredentialOkay() {

    }
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
