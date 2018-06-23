package com.oocl.trello.ticket.model;

import java.util.List;

public class Card {
    private String id;
    private String desc;
    private String name;
    private String idBoard;
    private String pos;
    private String urlSource;
    private String url;
    private List<String> idList;
    private List<String> idLabels;
    private List<String> idMembers;
    private Badges badge;

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public void setIdBoard(String idBoard) {
        this.idBoard = idBoard;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
