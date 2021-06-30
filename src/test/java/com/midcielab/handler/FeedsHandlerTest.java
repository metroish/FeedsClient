package com.midcielab.handler;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class FeedsHandlerTest {
    @Test
    public void testProcess() {
        assertFalse(FeedsHandler.getInstance().process());
    }
}
