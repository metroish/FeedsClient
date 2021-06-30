package com.midcielab.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtility {

    private static TimeUtility instance = new TimeUtility();
    private DateTimeFormatter dtfNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dtfComp = DateTimeFormatter.ofPattern(
            "[yyyy-MM-dd HH:mm:ss][EEE, dd MMM yyyy HH:mm:ss zzz][yyyy-MM-dd  HH:mm:ss][EEE, dd MMM yyyy HH:mm:ss Z][yyyy-MM-dd'T'HH:mm:ssXXX]");

    private TimeUtility() {
    }

    public static TimeUtility getInstance() {
        return instance;
    }

    public String getNow() {
        return LocalDateTime.now().format(dtfNow);
    }

    public boolean compareTime(String time1, String time2) {
        var ldt1 = LocalDateTime.parse(time1, dtfComp);
        var ldt2 = LocalDateTime.parse(time2, dtfComp);
        return ldt1.isAfter(ldt2);
    }
}
