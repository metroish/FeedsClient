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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeedsHandler {

    private static FeedsHandler instance = new FeedsHandler();
    private static final Logger logger = LogManager.getLogger();
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
        logger.info("===[ FeedsClient App Start ]===");
        if (retrieveFeedItems()) {
            feedItemsToMsg();
            performAction();
            saveState();
        }
        logger.info("===[ FeedsClient App End ]===");
        return this.actionResult;
    }

    private boolean retrieveFeedItems() {
        boolean hasNewItem = false;
        for (Feed feed : this.config.getFeed()) {
            logger.info("Retrieve Feed -> {}", feed.getName());
            Optional<HttpResponse<String>> resp = this.httpUtility.getUrlContent(feed.getUrl());
            if (resp.isEmpty()) {
                logger.info("Connection fail");
                feed.setResult("Connection fail");
            } else if (resp.get().statusCode() != 200) {
                logger.info("Server reponse error");
                logger.debug("Response headers -> {}", resp.get().headers());
                feed.setResult("Server reponse error");
            } else if (resp.get().body().length() == feed.getChecksum()) {
                logger.info("No new items to process");
                feed.setResult("Up-To-Date");
            } else {
                feed.setChecksum(resp.get().body().length());
                Optional<List<Item>> opl = ExtractUtility.getInstance().extract(resp.get().body());
                if (opl.isPresent()) {
                    logger.debug("Extract items -> {} ", opl.get().size());
                    List<Item> tempList = opl.get();
                    tempList.removeIf(
                            obj -> (TimeUtility.getInstance().compareTime(feed.getHandleTime(), obj.getPubDate())));
                    logger.debug("Earlier than ( {} ) items -> {}", feed.getHandleTime(), tempList.size());
                    if (!tempList.isEmpty()) {
                        feedItems.put(feed.getName(), tempList);
                        hasNewItem = true;
                        logger.info("Items to perform action -> {}", tempList.size());
                    }
                } else {
                    logger.info("Parsing items of feed fail");
                    feed.setResult("Parsing items of feed fail");
                }
            }
        }
        return hasNewItem;
    }

    private void feedItemsToMsg() {
        feedItems.forEach((name, listItems) -> {
            this.stringBuilder.append("== [ " + name + " ] == \n");
            listItems.forEach(item -> this.stringBuilder.append(item.getTitle() + "\n" + item.getLink() + "\n\n"));
        });
        logger.debug("Final msg content: \n {} \n", this.stringBuilder);
    }

    private void performAction() {
        String action = this.config.getAction();
        logger.info("Perform action -> {}", action);
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
                }, () -> this.actionResult = false);
                break;
            default:
                logger.info("No match action to perform");
        }
        logger.info("Action result -> {}", this.actionResult);
    }

    private void saveState() {
        this.config.getFeed().forEach(feed -> {
            if (feedItems.containsKey(feed.getName())) {
                if (this.actionResult) {
                    feed.setHandleTime(TimeUtility.getInstance().getNow());
                    feed.setResult("OK");
                    logger.debug("Update ( {} ) action result", feed.getName());
                } else {
                    feed.setChecksum(0);
                    feed.setResult("Action fail");
                    logger.debug("Reset ( {} ) checksum to retry", feed.getName());
                }
            }
        });
        ConfigUtility.getInsance().saveConfig(this.config);
        logger.info("Result save success");
    }
}
