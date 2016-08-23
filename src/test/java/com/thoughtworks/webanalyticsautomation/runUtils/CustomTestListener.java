package com.thoughtworks.webanalyticsautomation.runUtils;

/**
 * Created by: Anand Bagmar
 * Email: abagmar@gmail.com
 * Date: Dec 29, 2010
 * Time: 9:34:02 AM
 *
 * Copyright 2010 Anand Bagmar (abagmar@gmail.com).  Distributed under the Apache 2.0 License
 */

import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class CustomTestListener extends RunListener {
    private static String WAAT_START_LOG_MESSAGE = "\n\n" +
            "------------------------------------------------------------------------------------\n" +
            "----------------------------------" +
            " Start WAAT Log " +
            "----------------------------------\n" +
            "------------------------------------------------------------------------------------\n";

    private static String WAAT_END_LOG_MESSAGE = "\n\n" +
            "------------------------------------------------------------------------------------\n" +
            "-----------------------------------" +
            " End WAAT Log " +
            "-----------------------------------\n" +
            "------------------------------------------------------------------------------------\n";

    private static String WAAT_TEST_END_LOG_MESSAGE = "\n\n" +
            "-----------------------------------" +
            " TESTNAME " +
            "-----------------------------------\n";

    private Logger logger = Logger.getLogger(getClass());

    public void testRunStarted(Description description) throws Exception {
        logger.info(WAAT_START_LOG_MESSAGE);
        logger.info("Number of tests to execute: " + description.testCount());
    }

    public void testRunFinished(Result result) throws Exception {
        logger.info(WAAT_END_LOG_MESSAGE);
        logger.info("Number of tests executed: " + result.getRunCount());
    }

    public void testStarted(Description description) throws Exception {
        String testName = "Test name: " + description.getMethodName();
        testName += "\n\t------------------------------------------------------------\n";
        logger.info (testName);
    }

    public void testFinished(Description description) throws Exception {
        String testCompleteMessage = WAAT_TEST_END_LOG_MESSAGE.replace("TESTNAME", description.getMethodName() + ": PASS");
        logger.info(testCompleteMessage);
    }

    public void testFailure(Failure failure) throws Exception {
        String testCompleteMessage = WAAT_TEST_END_LOG_MESSAGE.replace("TESTNAME", failure.getMessage() + ": FAIL");
        logger.info(testCompleteMessage);
    }

    public void testAssumptionFailure(Failure failure) {
        System.out.println("Failed: " + failure.getDescription().getMethodName());
    }

    public void testIgnored(Description description) throws Exception {
        String testCompleteMessage = WAAT_TEST_END_LOG_MESSAGE.replace("TESTNAME", description.getMethodName() + ": SKIP");
        logger.info(testCompleteMessage);
    }
}
