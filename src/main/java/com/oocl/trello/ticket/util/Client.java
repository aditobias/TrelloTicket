package com.oocl.trello.ticket.util;

import com.oocl.trello.ticket.model.Card;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public final static Logger logger = Logger.getLogger(Client.class);

    public static HttpResponse get(String url) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            return client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }

        return null;
    }

    public void postCreate(String url, Card card) {

        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://api.trello.com/1/cards");
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("name", card.getName()));
            params.add(new BasicNameValuePair("desc", card.getDesc()));
            params.add(new BasicNameValuePair("pos", "top"));
            params.add(new BasicNameValuePair("idList", "5aefe1f98062b37abbcb95e0"));
            params.add(new BasicNameValuePair("idLabels", "5aeff50705baed7f89c9d876"));
            params.add(new BasicNameValuePair("idMembers", "5ac1e6bc961d1b718cd2317d"));
            params.add(new BasicNameValuePair("idMembers", "5a0b91fecdacfd463f72d094"));
            params.add(new BasicNameValuePair("idMembers", "56b05db57b949ea7b59ec0ce"));

            params.add(new BasicNameValuePair("urlSource", card.getUrlSource()));
            params.add(new BasicNameValuePair("keepFromSource", "all"));
            params.add(new BasicNameValuePair("key", "46308c25cef506f226fe7721d7b2d95f"));
            params.add(new BasicNameValuePair("token", "d2b62245631a432f9507505542dd0ab9992a96171d79b265a0f682e956b7b479"));
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

    public static String getURLSource(String url) throws IOException
    {
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }

}
