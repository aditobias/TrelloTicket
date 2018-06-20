package com.oocl.trello.ticket.service;

import com.oocl.trello.ticket.model.Config;
import com.oocl.trello.ticket.model.Ticket;
import com.oocl.trello.ticket.util.Client;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketProcessService {
    private final Config config;

    public TicketProcessService(Config config) {
        this.config = config;
    }

    public List<Ticket> getTicketFromPortal() throws IOException {

        List<Ticket> tickets = new ArrayList<>();


            String source = Client.getURLSource(this.config.getUrl());

//        File file = new File("sample.txt");
//        FileReader fileReader = new FileReader(file);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        StringBuffer stringBuffer = new StringBuffer();
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            stringBuffer.append(line);
//            stringBuffer.append("\n");
//        }
//        fileReader.close();
//        String source = stringBuffer.toString();

        Pattern rowStylePattern = Pattern.compile("\\<tr class=\"RowStyle\">(.*?)\\</tr>");
        Pattern alternatingRowStylePattern = Pattern.compile("\\<tr class=\"AlternatingRowStyle\">(.*?)\\</tr>");
        getTicketInformationInHTML(tickets, rowStylePattern.matcher(source));
        getTicketInformationInHTML(tickets, alternatingRowStylePattern.matcher(source));


        return tickets;
    }

    private void getTicketInformationInHTML(List<Ticket> tickets, Matcher m) {
        while (m.find()) {

            Pattern p1 = Pattern.compile("\\<td .*?>(.*?)\\</td>");
            Matcher m1 = p1.matcher(m.group(1));

            int index = 0;
            Ticket ticket = new Ticket();
            while (m1.find()) {
                switch (index) {
                    case 0:
                        String str = m.group(1);
                        String link = str.substring(str.indexOf("<a href=\"") + 9, str.indexOf("\" target=\"_blank\">"));
                        String number = str.substring(str.indexOf("blank\">") + 7, str.indexOf("</a>"));
                        ticket.setLink(link);
                        ticket.setNumber(number);
                    case 1:
                        ticket.setSeverity(m1.group(1));

                    case 2:
                        ticket.setCategory(m1.group(1));

                    case 3:
                        ticket.setSubject(m1.group(1));

                    case 4:
                        ticket.setStatus(m1.group(1));

                    case 5:
                        ticket.setCreateDate(m1.group(1));

                    case 6:
                        ticket.setResponseParty(m1.group(1));


                }

                index++;
            }
            tickets.add(ticket);

        }
    }
}
