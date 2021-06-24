package com.midcielab.handler;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.midcielab.model.Config;
import com.midcielab.model.Feed;
import com.midcielab.model.Item;
import com.midcielab.utility.ConfigUtility;
import com.midcielab.utility.ExtractUtility;
import com.midcielab.utility.HttpUtility;
import com.midcielab.utility.SmtpUtility;
import com.midcielab.utility.TimeUtility;

public class FeedsHandler {

    private static FeedsHandler instance = new FeedsHandler();
    private Config config;
    private HttpUtility httpUtility;
    private StringBuilder stringBuilder;
    private boolean actionResult;
    private Map<String, List<Item>> feedItems;

    private FeedsHandler() {
        this.config = ConfigUtility.getInsance().getConfig();
        this.httpUtility = HttpUtility.getInstance();
        this.stringBuilder = new StringBuilder();
        this.actionResult = false;
        this.feedItems = new HashMap<>();
    }

    public static FeedsHandler getInstance() {
        return instance;
    }

    public boolean process() {
        if (retreiveFeedItems()) {
            feedItemsToMsg();
            performAction();
            saveState();
        } else {
            return true;
        }
        return this.actionResult;
    }

    private boolean retreiveFeedItems() {
        boolean hasNewItem = false;
        for (Feed feed : this.config.getFeed()) {
            Optional<HttpResponse<String>> resp = this.httpUtility.getUrlContent(feed.getUrl());
            if (resp.isEmpty()) {
                feed.setResult("Connection fail, check server status.");
                continue;
            } else if (resp.get().statusCode() != 200) {
                feed.setResult("Retreive feed fail, RC = " + resp.get().statusCode());
                continue;
            } else if (resp.get().body().length() == feed.getChecksum()) {
                feed.setResult("OK, keep latest.");
                continue;
            } else {
                feed.setChecksum(resp.get().body().length());
                Optional<List<Item>> opl = ExtractUtility.getInstance().extract(resp.get().body().toString());
                if (opl.isPresent()) {
                    List<Item> tempList = opl.get();
                    tempList.removeIf(
                            obj -> (TimeUtility.getInstance().compareTime(feed.getHandleTime(), obj.getPubDate())));
                    if (tempList.size() > 0) {
                        feedItems.put(feed.getName(), tempList);
                        hasNewItem = true;
                    }
                } else {
                    feed.setResult("Parsing feed fail, check feed format");
                }
            }
        }
        return hasNewItem;
    }

    private void feedItemsToMsg() {
        feedItems.forEach((name, listItems) -> {
            this.stringBuilder.append("== [ " + name + " ] == \n");
            listItems.forEach(item -> {
                this.stringBuilder.append(item.getTitle() + "\n" + item.getLink() + "\n\n");
            });
        });
    }

    private void performAction() {
        String action = this.config.getAction();
        switch (action.trim()) {
            case "smtp":
                this.actionResult = SmtpUtility.getInstance().sendMail(this.config.getSmtp(),
                        this.stringBuilder.toString());
                break;
            case "line":
                Optional<HttpResponse<String>> resp = HttpUtility.getInstance().sendLineAPI(
                        this.config.getLine().getUrl(), this.stringBuilder.toString(),
                        this.config.getLine().getToken());
                resp.ifPresentOrElse(respObj -> {
                    if (respObj.statusCode() == 200) {
                        this.actionResult = true;
                    } else {
                        this.actionResult = false;
                    }
                }, () -> {
                    this.actionResult = false;
                });
                break;
        }
    }

    private void saveState() {
        this.config.getFeed().forEach(feed -> {
            if (feedItems.containsKey(feed.getName())) {
                if (this.actionResult) {
                    feed.setHandleTime(TimeUtility.getInstance().getNow());
                    feed.setResult("OK");
                } else {
                    feed.setChecksum(0);
                    feed.setResult("Fail on action perform");
                }
            }
        });
        ConfigUtility.getInsance().saveConfig(this.config);
    }
}
