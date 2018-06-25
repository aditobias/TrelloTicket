package com.oocl.trello.ticket.service;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Card;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.RestTemplateHttpClient;
import com.oocl.trello.ticket.model.TLabel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.julienvey.trello.impl.TrelloUrl.GET_BOARD_LABELS;
import static com.julienvey.trello.impl.TrelloUrl.createUrl;

public class TrelloService extends TrelloImpl implements Trello {


    private RestTemplateHttpClient httpClient;
    private String applicationKey;
    private String accessToken;

    public TrelloService(String applicationKey, String accessToken) {
        //actually no need for this
        super(applicationKey, accessToken);
        this.applicationKey = applicationKey;
        this.accessToken = accessToken;
        this.httpClient = new RestTemplateHttpClient();
    }

    //todo update card description only
    void updateCardDescriptionOnly(Card card){

    }
    //todo update card VIP label only
    void updateCardVIPLabel(Card card){

    }


    public List<TLabel> getBoardTLabels(String boardId, Argument... args) {
        List<TLabel> labels = Arrays
                .asList(get(createUrl(GET_BOARD_LABELS).params(args).asString(), TLabel[].class, boardId));
        for (TLabel label : labels) {
            label.setInternalTrello(this);
        }
        return labels;
    }

    private <T> T get(String url, Class<T> objectClass, String... params) {

        return this.httpClient.get(url, objectClass, enrichParams(params));
    }

    private String[] enrichParams(String[] params) {
        List<String> paramList = new ArrayList<>(Arrays.asList(params));
        paramList.add(applicationKey);
        paramList.add(accessToken);
        return paramList.toArray(new String[paramList.size()]);
    }
}
