package com.midcielab.utility;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class SmtpUtilityTest {
    @Test
    public void testSendMail() {
        assertNotNull(SmtpUtility.getInstance().sendMail(ConfigUtility.getInsance().getConfig().getSmtp(), "Test Report"));
    }
}
