package com.griddynamics.appiumexample.scenarios;

import com.google.inject.AbstractModule;
import com.griddynamics.appiumexample.Configuration;
import com.griddynamics.appiumexample.utils.ShellUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.griddynamics.appiumexample.Constants.APPIUM_URL;

public class AndroidDriverModule extends AbstractModule {

    private static final Logger log = Logger.getLogger(AndroidDriverModule.class.getName());

    @Override
    protected void configure() {
        AndroidDriver driver = getAndroidDriver();
        ShellUtils shellUtils = getShellUtils(driver);
        bind(AndroidDriver.class).toInstance(driver);
        bind(ShellUtils.class).toInstance(shellUtils);
    }

    private AndroidDriver getAndroidDriver() {
        log.info("Creating Android driver...");
        AndroidDriver driver;
        try {
            log.info("Appium url: " + APPIUM_URL + "/wd/hub");
            driver = new AndroidDriver<MobileElement>(new URL(APPIUM_URL + "/wd/hub"), Configuration.getCapabilities());
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Appium driver could not be initialized." + ex.getMessage());
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }

    private ShellUtils getShellUtils(AndroidDriver driver) {
        log.info("Creating ShellUtils instance...");
        return new ShellUtils(driver);
    }

}
