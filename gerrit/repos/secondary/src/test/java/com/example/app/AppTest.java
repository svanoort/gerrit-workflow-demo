package com.example.app;


import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Testcase that always fail after 15 sec so this build will crash unless tests are skipped
 */
public class AppTest {

    @Test
    public void fails() throws Exception {
        Thread.sleep(15000);
        fail("Always fails");
    }
}
