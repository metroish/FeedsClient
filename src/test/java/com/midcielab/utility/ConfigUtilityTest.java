package com.midcielab.utility;

import static org.junit.Assert.assertEquals;

import com.midcielab.model.Config;

import org.junit.Test;

public class ConfigUtilityTest {
    @Test
    public void test() {
        Config config = ConfigUtility.getInsance().getConfig();
        assertEquals("smtp-server", config.getSmtp().getServer());
        assertEquals(9527, config.getSmtp().getPort());
        assertEquals("kaif.io", config.getFeed().iterator().next().getName());
    }

}
