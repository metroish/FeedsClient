package com.midcielab.utility;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmtpUtilityTest {
    @Test
    public void testSendMail() {
        assertTrue(SmtpUtility.getInstance().sendMail(ConfigUtility.getInsance().getConfig().getSmtp(), "Test Report"));
    }
}
