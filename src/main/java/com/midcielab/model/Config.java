package com.midcielab.model;

import java.util.List;

public class Config {
    private String action;
    private String agent;
    private Smtp smtp;
    private Line line;
    private List<Feed> feed;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Smtp getSmtp() {
        return smtp;
    }

    public void setSmtp(Smtp smtp) {
        this.smtp = smtp;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Feed> getFeed() {
        return feed;
    }

    public void setFeed(List<Feed> feed) {
        this.feed = feed;
    }

}
