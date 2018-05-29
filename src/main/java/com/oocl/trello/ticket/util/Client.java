package com.oocl.trello.ticket.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public final static Logger logger = Logger.getLogger(Client.class);

    public static void get(String url) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }

    }

    public void post(String url) {

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://api.trello.com/1/cards");
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response1 = httpclient.execute(httppost);
            HttpEntity entity = response1.getEntity();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }


    }
}
