package com.midcielab.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeUtilityTest {
    @Test
    public void testCompareTime() {
        String now = TimeUtility.getInstance().getNow();
        assertTrue(TimeUtility.getInstance().compareTime(now, "1987-05-20 12:34:56"));
        // EEE, dd MMM yyyy HH:mm:ss zzz
        assertTrue(TimeUtility.getInstance().compareTime(now, "Sun, 20 Jun 2021 02:21:01 GMT"));
        // EEE, dd MMM yyyy HH:mm:ss Z
        assertTrue(TimeUtility.getInstance().compareTime(now, "Thu, 24 Jun 2021 00:00:00 +0000"));
        // yyyy-MM-dd'T'HH:mm:ssXXX
        assertTrue(TimeUtility.getInstance().compareTime(now, "2020-06-24T17:18:24+08:00"));
    }
}
