package com.oocl.trello.ticket.facade;

import com.google.gson.Gson;
import com.oocl.trello.ticket.model.Column;
import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.Label;
import com.oocl.trello.ticket.model.User;
import com.oocl.trello.ticket.util.Client;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TrelloFacade {

    private final Config config;

    public TrelloFacade(Config config) {
        this.config = config;
    }

    public void createTicketInTrello(){

    }

    public List<Label> getTrelloBoardLabels() {

        try {
            List<Label> labels = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/labels?key={1}&token={2}", this.config.getBoard(), this.config.getKey(), this.config.getToken());
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

    public List<User> getTrelloBoardMembers() {
        try {
            List<User> users = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/members?key={1}&token={2}", this.config.getBoard(), this.config.getKey(), this.config.getToken());
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

    public List<Column> getTrelloColumns() {
        try {
            List<Column> columns = new ArrayList<>();
            String URL = MessageFormat.format("https://api.trello.com/1/boards/{0}/lists?key={1}&token={2}", this.config.getBoard(), this.config.getKey(), this.config.getToken());
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
