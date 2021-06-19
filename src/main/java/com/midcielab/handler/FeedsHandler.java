package com.midcielab.handler;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import com.midcielab.model.Config;
import com.midcielab.model.Feed;
import com.midcielab.model.Item;
import com.midcielab.utility.ConfigUtility;
import com.midcielab.utility.ExtractUtility;
import com.midcielab.utility.HttpUtility;
import com.midcielab.utility.TimeUtility;

public class FeedsHandler {
    private Config config;
    private HttpUtility httpUtility;

    public FeedsHandler() {
        this.config = ConfigUtility.getInsance().getConfig();
        this.httpUtility = HttpUtility.getInstance();
    }

    public void process() {
        retreiveFeedItems();

    }

    private void retreiveFeedItems() {
        for (Feed feed : this.config.getFeed()) {
            Optional<HttpResponse<String>> resp = this.httpUtility.sendRequest(feed.getUrl(), "GET", "");
            if (resp.isEmpty()) {
                feed.setResult("Connection fail, check server status.");
                continue;
            } else if (resp.get().statusCode() != 200) {
                feed.setResult("Retreive feed fail, RC = " + resp.get().statusCode());
                continue;
            } else if (resp.get().body().length() == feed.getChecksum()) {
                feed.setHandleTime(TimeUtility.getInstance().getNow().toString());
                feed.setResult("OK");
                continue;
            } else {
                feed.setChecksum(resp.get().body().length());
                List<Item> tempList = ExtractUtility.getInstance().extract(resp.get().body().toString());
                tempList.removeIf(
                        obj -> (TimeUtility.getInstance().compareTime(obj.getPubDate(), feed.getHandleTime())));            
                feed.setItem(tempList);
            }
        }
    }

    private void feedAction() {
        String[] actions = this.config.getAction().split(",");
        for (String action : actions) {
            switch (action.toLowerCase().trim()) {
                case "smtp":
                    
                break;
                case "line":

                break;
            }
        }
    }

}
