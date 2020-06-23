package com.anandbagmar.webanalyticsautomation.scriptrunner.helper;

import com.anandbagmar.webanalyticsautomation.common.BROWSER;
import com.anandbagmar.webanalyticsautomation.runUtils.WebDriverUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.SkipException;

/**
 * Created by: Anand Bagmar
 * Email: abagmar@gmail.com
 * Date: Jan 4, 2011
 * Time: 10:38:05 AM
 * <p>
 * Copyright 2010 Anand Bagmar (abagmar@gmail.com).  Distributed under the Apache 2.0 License
 */

public class WebDriverScriptRunnerHelper extends ScriptRunnerHelper {

    private WebDriver driver;

    public WebDriverScriptRunnerHelper(Logger logger, BROWSER browser, String baseURL) {
        super(logger, browser, baseURL);
    }

    private void instantiateChromeDriver(Proxy proxy) {
        WebDriverUtils.getPathForChromeDriver();
        ChromeOptions chromeOptions = new ChromeOptions();
        if (null != proxy) {
            chromeOptions.setCapability(CapabilityType.PROXY, proxy);
            chromeOptions.addArguments("--ignore-ssl-errors=yes");
            chromeOptions.addArguments("--ignore-certificate-errors");
        }
        driver = new ChromeDriver(chromeOptions);
        driver.get(BASE_URL);
    }

    private void instantiateIEDriver(String os, Proxy proxy) {
        WebDriverUtils.instantiateIEDriver(os);
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        if (null != proxy) {
            internetExplorerOptions.setProxy(proxy);
        }
        driver = new InternetExplorerDriver(internetExplorerOptions);
        driver.get(BASE_URL);
    }

    private void instantiateEdgeDriver(String os, Proxy proxy) {
        WebDriverUtils.instantiateEdgeDriver(os);
        EdgeOptions edgeOptions = new EdgeOptions();
        if (null != proxy) {
            edgeOptions.setProxy(proxy);
        }
        driver = new EdgeDriver(edgeOptions);
        driver.get(BASE_URL);
    }

    private void instantiateFireFoxDriver(Proxy proxy) {
        WebDriverUtils.getPathForFirefoxDriver();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (null != proxy) {
            firefoxOptions.setCapability(CapabilityType.PROXY, proxy);
            firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        }

        driver = new FirefoxDriver(firefoxOptions);
        driver.get(BASE_URL);
    }

    @Override
    public void startDriverUsingProxy(Proxy proxy) {
        String os = System.getProperty("os.name").toLowerCase();
        logger.info("Starting WebDriver on OS: " + os + " for browser: " + browser.name());
        if (browser.equals(BROWSER.firefox)) {
            instantiateFireFoxDriver(proxy);
        } else if (browser.equals(BROWSER.edge)) {
            instantiateEdgeDriver(os, proxy);
        } else if (browser.equals(BROWSER.iehta)) {
            instantiateIEDriver(os, proxy);
        } else if (browser.equals(BROWSER.chrome)) {
            instantiateChromeDriver(proxy);
        }
        logger.info("Driver started: " + browser.name());
        logger.info("Page title: " + driver.getTitle());
    }

    @Override
    public void stopDriver() {
//        if (null != this.driver) {
//            driver.close();
//        }
        try {
            driver.quit();
        } catch (Exception e) {
            // quit the driver
        }
    }

    @Override
    public Object getDriverInstance() {
        if (null == driver) {
            logger.info("Driver is null");
        }
        return driver;
    }
}
