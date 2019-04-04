package com.griddynamics.appiumexample.screens;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ItemWidget extends Widget {

    public ItemWidget(WebElement element) {
        super(element);
    }

    private AndroidElement getSubItemById(String id){
        return (AndroidElement) getWrappedElement().findElement(By.id(id));
    }

    public String getTitle(){
        return getSubItemById("title_tv").getText().replaceFirst("Title: ", "");
    }
}
