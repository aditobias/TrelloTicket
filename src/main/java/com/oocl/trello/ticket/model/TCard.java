package com.oocl.trello.ticket.model;

import com.julienvey.trello.domain.Card;

import java.util.List;

public class TCard extends Card {

    private List<String> idLabels;

    public List<String> getIdLabels() {
        return idLabels;
    }

    public void setIdLabels(List<String> idLabels) {
        this.idLabels = idLabels;
    }
}
