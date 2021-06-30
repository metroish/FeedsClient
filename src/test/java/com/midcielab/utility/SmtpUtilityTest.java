package com.midcielab.utility;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class SmtpUtilityTest {
    @Test
    public void testSendMail() {
        assertFalse(
                SmtpUtility.getInstance().sendMail(ConfigUtility.getInsance().getConfig().getSmtp(), "Test Report"));
    }
}
