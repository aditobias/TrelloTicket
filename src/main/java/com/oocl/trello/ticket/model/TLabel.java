package com.oocl.trello.ticket.model;

import com.julienvey.trello.domain.Label;

public class TLabel extends Label {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
