package com.griddynamics.appiumexample.screens;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.griddynamics.appiumexample.Configuration;
import com.griddynamics.appiumexample.scenarios.AndroidDriverModule;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Guice;

import java.time.Duration;

public abstract class BaseScreen {

    @Inject
    protected AndroidDriver driver;

    public BaseScreen() {
        Injector injector = Configuration.getGuiceInjector();
        injector.injectMembers(this);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public abstract boolean isShown();

    public BaseScreen hideKeyboard() {
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
        return this;
    }
}
