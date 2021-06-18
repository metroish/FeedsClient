package com.midcielab.model;

import java.util.List;

public class Config {
    private Smtp smtp;
    private List<Feed> feed;

    public Smtp getSmtp() {
        return smtp;
    }
    public void setSmtp(Smtp smtp) {
        this.smtp = smtp;
    }
    public List<Feed> getFeed() {
        return feed;
    }
    public void setFeed(List<Feed> feed) {
        this.feed = feed;
    }
    
    
}
