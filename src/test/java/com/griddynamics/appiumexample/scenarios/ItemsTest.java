package com.griddynamics.appiumexample.scenarios;

import com.griddynamics.appiumexample.screens.AddItemScreen;
import com.griddynamics.appiumexample.screens.ItemDetailsScreen;
import com.griddynamics.appiumexample.screens.MainScreen;
import org.testng.annotations.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.testng.Assert.assertTrue;

public class ItemsTest extends AndroidBaseTest {

    @Test
    public void addNewItemTest() {
        driver.launchApp();
        MainScreen mainScreen = new MainScreen();
        assertTrue(mainScreen.isShown());
        assertThat(mainScreen.getAvailbleItemsCount()).isEqualTo(0);

        AddItemScreen addItemScreen = mainScreen.navigateToAddItemScreen();
        assertTrue(addItemScreen.isShown());

        addItemScreen.addTitle("Title1")
                .addDescription("Description1")
                .addMaker("Maker1")
                .setLHW(10, 20, 50).hideKeyboard().saveItem();

        assertTrue(mainScreen.isShown());
        assertThat(mainScreen.getAvailbleItemsCount()).isEqualTo(1);
        assertThat(mainScreen.getTitleOfItem(0)).isEqualTo("Title1");
    }

//    @Test
//    public void deleteExistingItemTest() {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
//        utils.putNewItem("Title2", "Maker2", "Description2", new int[]{100, 200, 300});
//        driver.launchApp();
//        MainScreen mainScreen = new MainScreen();
//        assertTrue(mainScreen.isShown());
//        assertThat(mainScreen.getAvailbleItemsCount()).isEqualTo(1);
//        assertThat(mainScreen.getTitleOfItem(0)).isEqualTo("Title2");
//
//        ItemDetailsScreen itemDetailsScreen = mainScreen.selectItem(0);
//        assertTrue(itemDetailsScreen.isShown());
//        mainScreen = itemDetailsScreen.deleteItem();
//        assertTrue(mainScreen.isShown());
//        assertThat(mainScreen.getAvailbleItemsCount()).isEqualTo(0);
//    }
}
