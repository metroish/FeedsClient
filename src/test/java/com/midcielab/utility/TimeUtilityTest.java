package com.midcielab.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeUtilityTest {
    @Test
    public void testCompareTime() {
        String now = TimeUtility.getInstance().getNow();
        assertTrue(TimeUtility.getInstance().compareTime(now, "1987-05-20 12:34:56"));
    }
}
