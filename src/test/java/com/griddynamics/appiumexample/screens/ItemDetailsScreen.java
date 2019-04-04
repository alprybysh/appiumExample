package com.griddynamics.appiumexample.screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ItemDetailsScreen extends AddItemScreen {

    @AndroidFindBy(id = "delete_item")
    private AndroidElement deleteButton;

    @Override
    public boolean isShown() {
        return super.isShown() && deleteButton.isDisplayed();
    }

    public MainScreen deleteItem(){
        deleteButton.click();
        return new MainScreen();
    }
}
