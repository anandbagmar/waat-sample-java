package com.anandbagmar.webanalyticsautomation.runUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.testng.SkipException;

import java.io.IOException;
import java.util.Arrays;

public class WebDriverUtils {
    private static final Logger logger = Logger.getLogger(WebDriverUtils.class);

    public static String getPathForChromeDriver() {
        int[] versionNamesArr = getChromeVersionsFor();
        if (versionNamesArr.length > 0) {
            int highestChromeVersion = Arrays.stream(versionNamesArr).max().getAsInt();
            String message = "ChromeDriver for Chrome version " + highestChromeVersion
                                     + "on device";
            logger.debug(message);
            WebDriverManager.chromedriver().version(String.valueOf(highestChromeVersion)).setup();
        } else {
            WebDriverManager.chromedriver().setup();
        }
        String path = WebDriverManager.chromedriver().getBinaryPath();
        System.setProperty("webdriver.chrome.driver", path);
        return path;
    }

    public static String getPathForFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        String firefoxDriverPath = WebDriverManager.firefoxdriver().getBinaryPath();
        System.out.println("FirefoxDriver path: " + firefoxDriverPath);
        System.setProperty("webdriver.firefox.driver", firefoxDriverPath);
        System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
        return firefoxDriverPath;
    }

    private static int[] getChromeVersionsFor() {
        CommandPrompt cmd = new CommandPrompt();
        String resultStdOut = null;
        try {
            resultStdOut = cmd.runCommandThruProcess("adb shell dumpsys package com.android.chrome | grep versionName");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] versionNamesArr = {};
        if (resultStdOut.contains("versionName=")) {
            String[] foundVersions = resultStdOut.split("\n");
            for (String foundVersion : foundVersions) {
                String version = foundVersion.split("=")[1].split("\\.")[0];
                String format = String.format("Found Chrome version - '%s'", version);
                logger.debug(format);
                versionNamesArr = ArrayUtils.add(versionNamesArr, Integer.parseInt(version));
            }
        } else {
            logger.debug(String.format("Chrome not found on device"));
        }
        return versionNamesArr;
    }

    public static String instantiateEdgeDriver(String os) {
        if (!os.contains("win")) {
            throw new SkipException("Skipping this test as Edge browser is NOT available on " + os);
        }
        WebDriverManager.edgedriver().setup();
        String path = WebDriverManager.edgedriver().getBinaryPath();
        System.out.println("EdgeDriver path: " + path);
        return path;
    }

    public static String instantiateIEDriver(String os) {
        if (!os.contains("win")) {
            throw new SkipException("Skipping this test as Internet Explorer browser is NOT available on " + os);
        }
        WebDriverManager.iedriver().setup();
        String path = WebDriverManager.iedriver().getBinaryPath();
        System.out.println("IEDriver path: " + path);
        return path;
    }
}
