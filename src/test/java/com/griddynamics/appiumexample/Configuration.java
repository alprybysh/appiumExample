package com.griddynamics.appiumexample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.griddynamics.appiumexample.scenarios.AndroidDriverModule;
import com.griddynamics.appiumexample.utils.bitbar.BitbarCommonAPI;
import com.griddynamics.appiumexample.utils.kobiton.KobitonAPI;
import com.griddynamics.appiumexample.utils.kobiton.KobitonDevice;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Optional;
import java.util.logging.Logger;

import static com.griddynamics.appiumexample.Constants.*;
import static com.griddynamics.appiumexample.utils.UploadAppHandler.uploadAppToBitbarAndGetUUID;
import static com.griddynamics.appiumexample.utils.UploadAppHandler.uploadAppToBrowserstackAndGetId;
import static com.griddynamics.appiumexample.utils.UploadAppHandler.uploadAppToSaucelabsAndGetId;

public class Configuration {
    private static final Logger LOG = Logger.getLogger(Configuration.class.getName());

    private static Injector injector;

    private static AppiumDriverLocalService appiumService;

    public static boolean isLocalRun() {
        return "local".equals(RUN_TYPE);
    }

    public static boolean isAwsRun() {
        return "aws".equals(RUN_TYPE);
    }

    public static boolean isBitbarRemoteRun() {
        return "bitbarRemote".equals(RUN_TYPE);
    }


    public static Injector getGuiceInjector() {
        if (injector == null) {
            injector = Guice.createInjector(new AndroidDriverModule());
        }
        return injector;
    }

    public static AppiumDriverLocalService getAppiumDriverLocalService() {
        if (appiumService == null) {
            LOG.info("Creating local Appium service...");
            appiumService = AppiumDriverLocalService.
                    buildService(new AppiumServiceBuilder().
                            withArgument(GeneralServerFlag.RELAXED_SECURITY).
                            usingPort(LOCAL_PORT));
        }
        return appiumService;
    }


    public static DesiredCapabilities getCapabilities() {
        switch (RUN_TYPE) {
            case "aws":
                return getAWSCloudCapabilities();
            case "kobiton":
                return getKobitonCapabilities();
            case "bitbarLocal":
                return getBitbarLocalCapabilities();
            case "bitbarRemote":
                return getBitbarRemoteCapabilities();
            case "saucelabsRemote":
                return getSaucelabsRealDeviceRunCapabilities();
            case "browserstackRemote":
                return getBrowserstackCapabilities();
            default:
                return getLocalCapabilities();
        }
    }

    private static DesiredCapabilities getLocalCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device", "Android");
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", APP_PATH);
        capabilities.setCapability("appPackage", "com.example.sharingapp");
        capabilities.setCapability("appActivity", ".MainActivity");
        capabilities.setCapability("autoLaunch", "false");
        capabilities.setCapability("noReset", "true");
        // long timeout is needed for debugging purposes
        capabilities.setCapability("newCommandTimeout", 600);
        LOG.info("Desired capabilities for local run : " + capabilities);
        return capabilities;
    }

    /**
     * Required capabilities:
     * deviceName - device name of available device
     * platformName - Android/ios platform
     * platformVersion - version of Android/ios
     * app - path to the app, that is stored remotely
     */
    private static DesiredCapabilities getKobitonCapabilities() {
        String pathToRemoteApp = System.getProperty("kobitonRemotePath");
        KobitonDevice availableDevice = KobitonAPI.getAvailableDevice(ANDROID_PLATFORM_NAME);

        LOG.info("Kobiton remote app path:" + pathToRemoteApp);
        LOG.info("Kobiton device name: " + availableDevice.getDeviceName());
        LOG.info("Kobiton android version: " + availableDevice.getPlatformVersion());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", availableDevice.getDeviceName());
        capabilities.setCapability("sessionName", "Automation test session");
        capabilities.setCapability("sessionDescription", "");
        capabilities.setCapability("deviceOrientation", "portrait");
        capabilities.setCapability("captureScreenshots", true);
        capabilities.setCapability("deviceGroup", "KOBITON");
        capabilities.setCapability("platformVersion", availableDevice.getPlatformVersion());
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", pathToRemoteApp);
        capabilities.setCapability("appPackage", "com.example.sharingapp");
        capabilities.setCapability("appActivity", ".MainActivity");
        capabilities.setCapability("autoLaunch", "false");
        capabilities.setCapability("noReset", "true");
        // long timeout is needed for debugging purposes
        capabilities.setCapability("newCommandTimeout", 600);
        LOG.info("Desired capabilities for Kobiton run : " + capabilities);
        return capabilities;
    }

    /**
     * This method runs before any other method.
     * <p>
     * Appium follows a client - server model:
     * We are setting up our appium client in order to connect to Device Farm's appium server.
     * <p>
     * We do not need to and SHOULD NOT set our own DesiredCapabilities
     * Device Farm creates custom settings at the server level. Setting your own DesiredCapabilities
     * will result in unexpected results and failures.
     */
    private static DesiredCapabilities getAWSCloudCapabilities() {
        return new DesiredCapabilities();
    }

    /**
     * Desired capabilities for remote bitbar run.
     * Required:
     * platformName
     * deviceName
     * app - don't change path to the .apk and .apk name. Bitbar stores you uploaded app as sharingApp.apk
     */
    private static DesiredCapabilities getBitbarRemoteCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Android Device");
        desiredCapabilities.setCapability("app", System.getProperty("user.dir") + "/application.apk");

        LOG.info("Desired capabilities for Bitbar remote run : " + desiredCapabilities);
        return desiredCapabilities;
    }

    /**
     * Required capabilities:
     * testdroid_apiKey - your Bitbar API key, can be found in My Account
     * testdroid_project - unique name
     * testdroid_testrun - name of test run
     * testdroid_app - uploaded app address
     * testdroid_target - Android/ios
     * testdroid_device - device name from Bitbar cloud
     */
    private static DesiredCapabilities getBitbarLocalCapabilities() {
        String BITBAR_API_KEY = System.getProperty("apiKey");
        String BITBAR_UPLOADED_APP_PATH = uploadAppToBitbarAndGetUUID(BITBAR_API_KEY);
        String BITBAR_CREATE_NEW_PROJECT = BitbarCommonAPI.createProject(BITBAR_API_KEY, ANDROID_PLATFORM_NAME.toUpperCase());

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Android Device");
        desiredCapabilities.setCapability("testdroid_apiKey", BITBAR_API_KEY);
        desiredCapabilities.setCapability("testdroid_project", BITBAR_CREATE_NEW_PROJECT);
        desiredCapabilities.setCapability("testdroid_testrun", "Demo1");
        desiredCapabilities.setCapability("testdroid_app", BITBAR_UPLOADED_APP_PATH);
        desiredCapabilities.setCapability("testdroid_device", "LG Google Nexus 5 6.0 -EU");
        desiredCapabilities.setCapability("testdroid_target", "Android");
        desiredCapabilities.setCapability("testdroid_findDevice", "true");

        LOG.info("Desired capabilities for Bitbar local run : " + desiredCapabilities);
        return desiredCapabilities;
    }

    /**
     * Required capabilities:
     * platformName - platform name - Android/ios
     * testobject_api_key - user secret api key
     * testobject_app_id - uploaded app id
     * */
    private static DesiredCapabilities getSaucelabsRealDeviceRunCapabilities() {
        String SAUCELABS_API_KEY = System.getProperty("apiKey");
        String SAUCELABS_USERNAME = System.getProperty("userName");
        String SAUCELABS_UPLOADED_APP_ID = uploadAppToSaucelabsAndGetId(SAUCELABS_USERNAME,SAUCELABS_API_KEY);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName","Android");
        desiredCapabilities.setCapability("testobject_api_key", SAUCELABS_API_KEY);
        desiredCapabilities.setCapability("testobject_app_id", SAUCELABS_UPLOADED_APP_ID);

        LOG.info("Desired capabilities for TestObject(testing on real devices - part of Saucrelabs) run : " + desiredCapabilities);
        return desiredCapabilities;
    }

    /**
     * Required capabilities:
     * device - real device name
     * os_version - version of device
     * app - uploaded app number. Should be in format: "bs://<hashed app-id>" or custom_id
     * Non required capabilities:
     * browserstack.networkLogs - enable collecting performance logs
     * browserstack.debug - enable collecting visual logs
     * */
    private static DesiredCapabilities getBrowserstackCapabilities(){
        String BROWSERSTACK_USERNAME = Optional.ofNullable(System.getProperty("userName"))
                .orElseThrow(()-> new RuntimeException("user name for Browserstack is not set!"));
        String BROWSERSTACK_ACCESSKEY =  Optional.ofNullable(System.getProperty("accessKey"))
                .orElseThrow(()-> new RuntimeException("access key for Browserstack is not set!"));
        String uploadedAppId = uploadAppToBrowserstackAndGetId(BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESSKEY);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("device", "Google Nexus 6");
        desiredCapabilities.setCapability("os_version", "6.0");
        desiredCapabilities.setCapability("app", uploadedAppId);
        desiredCapabilities.setCapability("browserstack.debug", "true");
        desiredCapabilities.setCapability("browserstack.networkLogs", "true");
        desiredCapabilities.setCapability("realMobile", "true");
        desiredCapabilities.setCapability("browserstack.appium_version", "1.8.0");

        LOG.info("Desired capabilities for Browserstack run : " + desiredCapabilities);
        return desiredCapabilities;
    }

    public static boolean isMemoryDumpEnabled() {
        boolean result = NumberUtils.isCreatable(TOTAL_MEMORY) && NumberUtils.isCreatable(THRESHOLD);
        if (result) {
            LOG.info("Passed memory parameters is valid, memory checks will be executed");
        } else {
            LOG.info("Passed memory parameters is not valid, memory checks will not be executed");
        }
        return result;
    }
}
