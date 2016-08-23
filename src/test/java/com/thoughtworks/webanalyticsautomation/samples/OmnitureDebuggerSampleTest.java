package com.thoughtworks.webanalyticsautomation.samples;

import com.thoughtworks.webanalyticsautomation.Controller;
import com.thoughtworks.webanalyticsautomation.Engine;
import com.thoughtworks.webanalyticsautomation.Result;
import com.thoughtworks.webanalyticsautomation.Status;
import com.thoughtworks.webanalyticsautomation.common.BROWSER;
import com.thoughtworks.webanalyticsautomation.common.TestBase;
import com.thoughtworks.webanalyticsautomation.common.Utils;
import com.thoughtworks.webanalyticsautomation.inputdata.InputFileType;
import com.thoughtworks.webanalyticsautomation.plugins.WebAnalyticTool;
import com.thoughtworks.webanalyticsautomation.scriptrunner.WebDriverScriptRunner;
import com.thoughtworks.webanalyticsautomation.scriptrunner.helper.WebDriverScriptRunnerHelper;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static com.thoughtworks.webanalyticsautomation.Controller.getInstance;

/**
 * Created by: Anand Bagmar
 * Email: abagmar@gmail.com
 * Date: Feb 2, 2011
 * Time: 4:23:29 PM
 *
 * Copyright 2010 Anand Bagmar (abagmar@gmail.com).  Distributed under the Apache 2.0 License
 */

public class OmnitureDebuggerSampleTest extends TestBase {
    private Logger logger = Logger.getLogger(getClass());
    private Engine engine;
    private WebAnalyticTool webAnalyticTool = WebAnalyticTool.OMNITURE_DEBUGGER;
    private InputFileType inputFileType = InputFileType.XML;
    private boolean keepLoadedFileInMemory = true;
    private String log4jPropertiesAbsoluteFilePath = Utils.getAbsolutePath(new String[] {"src", "main", "resources", "log4j.properties"});
    private String inputDataFileName = Utils.getAbsolutePath(new String[] {"src", "test", "sampledata", "TestData.xml"});
    private String actionName = "OpenUpcomingPage_OmnitureDebugger_Selenium";
    private WebDriverScriptRunnerHelper webDriverScriptRunnerHelper;
    private WebDriver driverInstance;

//    @Test
//    public void captureAndVerifyDataReportedToWebAnalytics_OmnitureDebugger_Selenium_IE() throws Exception {
//        captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER.iehta);
//    }

    @Test
    public void captureAndVerifyDataReportedToWebAnalytics_OmnitureDebugger_Selenium_Firefox() throws Exception {
        captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER.firefox);
    }

    private void captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER browser) throws Exception {
        String baseURL = "http://digg.com";
        String navigateToURL = baseURL + "/channel/sports";

        engine = getInstance(webAnalyticTool, inputFileType, keepLoadedFileInMemory, log4jPropertiesAbsoluteFilePath);
        engine.enableWebAnalyticsTesting(actionName);

        startSeleniumDriver(browser, baseURL);
        driverInstance.get(navigateToURL);

        Result verificationResult = engine.verifyWebAnalyticsData (inputDataFileName, actionName, new WebDriverScriptRunner(driverInstance));

        Assert.assertNotNull("Verification status should NOT be NULL", verificationResult.getVerificationStatus());
        Assert.assertNotNull("Failure details should NOT be NULL", verificationResult.getListOfErrors());
        logVerificationErrors(verificationResult);
        Assert.assertEquals("Verification status should be PASS", Status.FAIL, verificationResult.getVerificationStatus());
    }

    private void startSeleniumDriver(BROWSER browser, String baseURL) {
        webDriverScriptRunnerHelper = new WebDriverScriptRunnerHelper(logger, browser, baseURL);
        webDriverScriptRunnerHelper.startDriver();
        driverInstance = (WebDriver) webDriverScriptRunnerHelper.getDriverInstance();
    }

    @Before
    public void setup () {
        Controller.reset();
    }

    @After
    public void tearDown() throws Exception {
        if (null != engine) {
            engine.disableWebAnalyticsTesting();
        }
        webDriverScriptRunnerHelper.stopDriver();
    }
}
