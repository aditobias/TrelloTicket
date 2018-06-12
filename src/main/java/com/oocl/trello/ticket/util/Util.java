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

    public static List<Label> getTrelloBoardLabels() {

        try {
            List<Label> labels = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/labels?key={1}&token={2}", config.getBoard(), config.getKey(), config.getToken());
            HttpResponse response = Client.get(URL);

            BufferedReader rd = null;
            if (response != null) {
                rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
            }

            StringBuilder result = new StringBuilder();
            String output;
            while ((output = rd.readLine()) != null) {
                result.append(output);

                JSONArray array = new JSONArray(output);
                Gson gson = new Gson();
                for(int i = 0 ; i < array.length() ; i++){
                    JSONObject labelObject = array.getJSONObject(i);
                    Label label = gson.fromJson(labelObject.toString(), Label.class);
                    labels.add(label);
                }
            }
            return labels;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static List<User> getTrelloBoardMembers() {
        try {
            List<User> users = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/members?key={1}&token={2}", config.getBoard(), config.getKey(), config.getToken());
            HttpResponse response = Client.get(URL);

            BufferedReader rd = null;
            if (response != null) {
                rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
            }

            StringBuilder result = new StringBuilder();
            String output;
            if (rd != null) {
                while ((output = rd.readLine()) != null) {
                    result.append(output);

                    JSONArray array = new JSONArray(output);
                    Gson gson = new Gson();
                    for(int i = 0 ; i < array.length() ; i++){
                        JSONObject userObject = array.getJSONObject(i);
                        User user = gson.fromJson(userObject.toString(), User.class);
                        users.add(user);
                    }
                }
            }
            return users;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Column> getTrelloColumns() {
        try {
            List<Column> columns = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/lists?key={1}&token={2}", config.getBoard(), config.getKey(), config.getToken());
            HttpResponse response = Client.get(URL);

            BufferedReader rd = null;
            if (response != null) {
                rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
            }

            StringBuilder result = new StringBuilder();
            String output;
            if (rd != null) {
                while ((output = rd.readLine()) != null) {
                    result.append(output);

                    JSONArray array = new JSONArray(output);
                    Gson gson = new Gson();
                    for(int i = 0 ; i < array.length() ; i++){
                        JSONObject columnObject = array.getJSONObject(i);
                        Column column = gson.fromJson(columnObject.toString(), Column.class);
                        columns.add(column);
                    }
                }
            }
            return columns;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
