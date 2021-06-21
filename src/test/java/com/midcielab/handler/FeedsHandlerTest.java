package com.midcielab.handler;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class FeedsHandlerTest {
    @Test
    public void testProcess() {
        assertNotNull(FeedsHandler.getInstance().process());
    }
}
