package com.griddynamics.appiumexample.scenarios;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.griddynamics.appiumexample.Configuration;
import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.perfomance.*;
import com.griddynamics.appiumexample.utils.ShellUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static com.griddynamics.appiumexample.Configuration.isAwsRun;
import static com.griddynamics.appiumexample.Configuration.isLocalRun;
import static com.griddynamics.appiumexample.Configuration.isBitbarRemoteRun;

public class AndroidBaseTest {

    @Inject
    private Logger log;

    @Inject
    protected AndroidDriver driver;

    @Inject
    protected ShellUtils utils;


    private static AppiumDriverLocalService localService;

    @BeforeSuite
    public void startService() {
        if (isLocalRun()) {
            localService = Configuration.getAppiumDriverLocalService();
            localService.start();
        }
    }


    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.closeApp();
            driver.quit();
        }
    }

    @AfterSuite(dependsOnMethods = {"tearDown"})
    public void stopService() {
        if (isLocalRun()) {
            localService.stop();
        }
    }

    @BeforeMethod
    public void initDriver() {
        Injector injector = Configuration.getGuiceInjector();
        injector.injectMembers(this);
    }

    @BeforeMethod(dependsOnMethods = {"initDriver"})
    public void setTestApp() {
        if (isLocalRun()) {
            driver.resetApp();
            log.info(PerfProcStats.cleanProcStats(utils));
            log.info(PerfBatteryStat.cleanBatteryStats(utils));
        }
    }

    @AfterMethod(alwaysRun = true)
    public void dumpPerformanceData(ITestResult testResult) {
        if (isLocalRun()) {
            String deviceName = driver.getCapabilities().getCapability("deviceName").toString();
            String appVersion = utils.getAppVersion();
            TestStatus testStatus = new TestStatus(testResult, deviceName, appVersion);
            PerfMemInfo.dumpMemInfo(testStatus, utils);
            PerfUiInfo.dumpUiInfo(testStatus, utils);
            PerfCpuInfo.dumpCpuInfo(testStatus, utils);
            PerfProcStats.dumpProcStatsInfo(testStatus, utils);
            PerfBatteryStat.dumpBatteryStats(testStatus, utils);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void makeScreenshot(ITestResult testResult) {
        if (!testResult.isSuccess()) {
            takeScreenshot(testResult.getMethod().getMethodName());
        }
    }


    public void takeScreenshot(String screenshotName) {
        if (isBitbarRemoteRun()) {
            String fullFileName = System.getProperty("user.dir") + "/screenshots/" + screenshotName + ".png";
            log.info("Taking screenshot...");
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            try {
                File testScreenshot = new File(fullFileName);
                FileUtils.copyFile(scrFile, testScreenshot);
                log.info("Screenshot stored to " + testScreenshot.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isAwsRun()) {
            String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", screenshotName)));
        }
    }
}
