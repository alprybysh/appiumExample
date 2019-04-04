package com.griddynamics.appiumexample.screens;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class MainScreen extends BaseScreen {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sharing App\")")
    private AndroidElement header;

    @AndroidFindBy(className = "android.widget.ImageButton")
    private AndroidElement plusButton;

    @AndroidFindBy(id = "item_box")
    private List<ItemWidget> items;


    @Override
    public boolean isShown() {
        return Stream.of(header, plusButton).allMatch(AndroidElement::isDisplayed);
    }

    public AddItemScreen navigateToAddItemScreen() {
        plusButton.click();
        return new AddItemScreen();
    }

    public ItemDetailsScreen selectItem(int index){
        TouchAction action = new TouchAction(driver);
        WebElement item = items.get(index).getWrappedElement();
        action.longPress(new LongPressOptions().withElement(new ElementOption().withElement(item))
                .withDuration(Duration.ofSeconds(2)));
        action.perform();
        return new ItemDetailsScreen();
    }

    public int getAvailbleItemsCount(){
        System.out.println(items.size());
        return items.size();
    }

    public String getTitleOfItem(int index){
       return items.get(index).getTitle();
    }

    @Override
    public MainScreen hideKeyboard(){
        return (MainScreen) super.hideKeyboard();
    }
}
