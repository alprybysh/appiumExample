package com.griddynamics.appiumexample.screens;

import java.util.stream.Stream;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class AddItemScreen extends BaseScreen {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sharing App\")")
    private AndroidElement header;

    @AndroidFindBy(id = "save_button")
    private AndroidElement saveButton;

    @AndroidFindBy(id = "title")
    private AndroidElement title;

    @AndroidFindBy(id = "maker")
    private AndroidElement maker;

    @AndroidFindBy(id = "description")
    private AndroidElement description;

    @AndroidFindBy(id = "length")
    private AndroidElement length;

    @AndroidFindBy(id = "width")
    private AndroidElement width;

    @AndroidFindBy(id = "height")
    private AndroidElement height;


    @Override
    public boolean isShown() {
        return Stream.of(header, saveButton).allMatch(AndroidElement::isDisplayed);
    }

    public MainScreen goBack() {
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        return new MainScreen();
    }

    public MainScreen saveItem() {
        saveButton.click();
        return new MainScreen();
    }

    public AddItemScreen addTitle(String textTitle) {
        title.setValue(textTitle);
        return this;
    }

    public AddItemScreen addMaker(String textMaker) {
        maker.setValue(textMaker);
        return this;
    }

    public AddItemScreen addDescription(String textDescription) {
        description.setValue(textDescription);
        return this;
    }

    public AddItemScreen setLHW(int l, int w, int h) {
        length.setValue(String.valueOf(l));
        width.setValue(String.valueOf(w));
        height.setValue(String.valueOf(h));
        return this;
    }

    @Override
    public AddItemScreen hideKeyboard(){
        return (AddItemScreen) super.hideKeyboard();
    }
}
