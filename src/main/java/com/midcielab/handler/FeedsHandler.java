package com.midcielab.handler;

import java.net.http.HttpResponse;
import java.util.Optional;

import com.midcielab.model.Config;
import com.midcielab.model.Feed;
import com.midcielab.utility.ConfigUtility;
import com.midcielab.utility.HttpUtility;

public class FeedsHandler {
    private Config config;
    private HttpUtility httpUtility;

    public FeedsHandler() {
        this.config = ConfigUtility.getInsance().getConfig();
        this.httpUtility = HttpUtility.getInstance();
    }

    public void process() {
        for (Feed feed : this.config.getFeed()) {
            Optional<HttpResponse<String>> resp = this.httpUtility.sendRequest(feed.getUrl(), "GET", "");
            if (resp.isEmpty()) {
                continue;
            } else if (resp.get().statusCode() != 200) {
                continue;
            } else if (resp.get().body().length() == feed.getChecksum()) {
                continue;
            } else {
                feed.setContent(resp.get().body());
                feed.setChecksum(resp.get().body().length());
            }
        }
    }
}
