package com.anandbagmar.webanalyticsautomation.samples;

import com.anandbagmar.webanalyticsautomation.Controller;
import com.anandbagmar.webanalyticsautomation.Engine;
import com.anandbagmar.webanalyticsautomation.Result;
import com.anandbagmar.webanalyticsautomation.Status;
import com.anandbagmar.webanalyticsautomation.common.BROWSER;
import com.anandbagmar.webanalyticsautomation.common.TestBase;
import com.anandbagmar.webanalyticsautomation.common.Utils;
import com.anandbagmar.webanalyticsautomation.inputdata.InputFileType;
import com.anandbagmar.webanalyticsautomation.plugins.WebAnalyticTool;
import com.anandbagmar.webanalyticsautomation.scriptrunner.WebDriverScriptRunner;
import com.anandbagmar.webanalyticsautomation.scriptrunner.helper.WebDriverScriptRunnerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Created by: Anand Bagmar
 * Email: abagmar@gmail.com
 * Date: Feb 2, 2011
 * Time: 4:23:29 PM
 * <p>
 * Copyright 2010 Anand Bagmar (abagmar@gmail.com).  Distributed under the Apache 2.0 License
 */

public class OmnitureDebuggerSampleTest extends TestBase {
    private final Logger logger = Logger.getLogger(getClass());
    private Engine engine;
    private final WebAnalyticTool webAnalyticTool = WebAnalyticTool.OMNITURE_DEBUGGER;
    private final InputFileType inputFileType = InputFileType.XML;
    private final boolean keepLoadedFileInMemory = true;
    private final String log4jPropertiesAbsoluteFilePath = Utils.getAbsolutePath(new String[]{"resources", "log4j.properties"});
    private final String inputDataFileName = Utils.getAbsolutePath(new String[]{"src", "test", "sampledata", "TestData.xml"});
    private final String actionName = "OpenUpcomingPage_OmnitureDebugger_Selenium";
    private WebDriverScriptRunnerHelper webDriverScriptRunnerHelper;
    private WebDriver driverInstance;

    @Test
    public void captureAndVerifyDataReportedToWebAnalytics_OmnitureDebugger_Selenium_Firefox() throws Exception {
        captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER.firefox);
    }

    @Test
    public void captureAndVerifyDataReportedToWebAnalytics_OmnitureDebugger_Selenium_Chrome() throws Exception {
        captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER.chrome);
    }

    private void captureAndVerifyDataReportedToWebAnalytics_Omniture_Selenium(BROWSER browser) throws Exception {
        String baseURL = "https://digg.com";
        String navigateToURL = baseURL + "/channel/sports";

        engine = Controller.getInstance(webAnalyticTool, inputFileType, keepLoadedFileInMemory, log4jPropertiesAbsoluteFilePath);
        engine.enableWebAnalyticsTesting(actionName);

        startWebDriver(browser, baseURL);
        driverInstance.get(navigateToURL);

        Result verificationResult = engine.verifyWebAnalyticsData(inputDataFileName, actionName, new WebDriverScriptRunner(driverInstance));

        assertNotNull(verificationResult.getVerificationStatus(), "Verification status should NOT be NULL");
        assertNotNull(verificationResult.getListOfErrors(), "Failure details should NOT be NULL");
        logVerificationErrors(verificationResult);
        Assert.assertEquals(verificationResult.getVerificationStatus(), Status.FAIL, "Verification status should be FAIL");
    }

    @BeforeMethod
    public void setup() {
        Controller.reset();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        engine.disableWebAnalyticsTesting();
        webDriverScriptRunnerHelper.stopDriver();
    }
}
