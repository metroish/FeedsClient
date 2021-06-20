package com.midcielab.handler;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FeedsHandlerTest {
    @Test
    public void testProcess() {
        assertTrue(FeedsHandler.getInstance().process());
    }
}
