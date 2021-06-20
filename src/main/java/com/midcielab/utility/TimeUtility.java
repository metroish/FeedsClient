package com.midcielab.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtility {

    private static TimeUtility instance = new TimeUtility();
    private DateTimeFormatter dtfNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dtfComp = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss][EEE, dd MMM yyyy HH:mm:ss zzz][yyyy-MM-dd  HH:mm:ss]");

    private TimeUtility() {
    }

    public static TimeUtility getInstance() {
        return instance;
    }

    public String getNow() {
        return LocalDateTime.now().format(dtfNow).toString();
    }

    public boolean compareTime(String time1, String time2) {
        LocalDateTime ldt1 = LocalDateTime.parse(time1, dtfComp);
        LocalDateTime ldt2 = LocalDateTime.parse(time2, dtfComp);
        return ldt1.isAfter(ldt2);
    }
}