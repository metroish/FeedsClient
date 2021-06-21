package com.midcielab.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import com.midcielab.model.Config;
import com.midcielab.model.Feed;
import org.junit.Test;

public class ConfigUtilityTest {
    @Test
    public void testGetConfig() {
        Config config = ConfigUtility.getInsance().getConfig();
        assertEquals("smtp", config.getAction());
        assertEquals("smtp-server", config.getSmtp().getServer());
        assertEquals(9527, config.getSmtp().getPort());
        assertEquals("https://notify-api.line.me/api/notify", config.getLine().getUrl());
        assertEquals("kaif.io", config.getFeed().iterator().next().getName());
        assertTrue(config.getFeed().iterator().next().getChecksum() >= 0);
    }

    @Test
    public void testSaveConfig() {
        Config configBefore = ConfigUtility.getInsance().getConfig();
        configBefore.setAction("By Test");
        List<Feed> beforeFeeds = configBefore.getFeed();
        beforeFeeds.forEach(feed -> {
            feed.setResult("By Test");
        });
        ConfigUtility.getInsance().saveConfig(configBefore);

        Config configAfter = ConfigUtility.getInsance().getConfig();
        assertEquals("By Test", configAfter.getAction());
        List<Feed> afterFeeds = configAfter.getFeed();
        afterFeeds.forEach(feed -> {
            assertEquals("By Test", feed.getResult());
        });

        Config configRollback = ConfigUtility.getInsance().getConfig();
        configRollback.setAction("smtp");
        List<Feed> rollbackFeeds = configRollback.getFeed();
        rollbackFeeds.forEach(feed -> {
            feed.setResult("OK");
        });
        ConfigUtility.getInsance().saveConfig(configRollback);
    }
}
