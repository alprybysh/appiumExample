package com.griddynamics.appiumexample.utils;

import com.griddynamics.appiumexample.utils.bitbar.BitbarFileUploader;
import com.griddynamics.appiumexample.utils.browserstack.BrowserstackAPI;
import com.griddynamics.appiumexample.utils.saucelabs.TestObjectApi;

import java.io.IOException;
import java.util.logging.Logger;

import static com.griddynamics.appiumexample.Constants.APPIUM_URL;
import static com.griddynamics.appiumexample.Constants.APP_PATH;

public class UploadAppHandler {
    private static final Logger LOG = Logger.getLogger(UploadAppHandler.class.getName());

    public static String uploadAppToBrowserstackAndGetId(String userName, String accessKey) {
        LOG.info("Uploading " + APP_PATH + " to Browserstack Cloud");
        String fileUUID = "";
        try{
            fileUUID = BrowserstackAPI.uploadApp(APP_PATH, userName, accessKey);
        } catch (IOException ex){
            throw new RuntimeException("API call 'uploadApp' to Browserstack failed with exception: " + ex.toString());
        }
        LOG.info("File uploaded. File UUID is " + fileUUID);
        return fileUUID;
    }

    public static String uploadAppToSaucelabsAndGetId(String userName, String apiKey) {
        LOG.info("Uploading " + APP_PATH + " to Saucelabs Cloud");
        String fileUUID = "";
        try{
            fileUUID = TestObjectApi.uploadApp(APP_PATH, userName, apiKey);
        } catch (IOException ex){
            throw new RuntimeException("API call 'uploadApp' to Saucelabs failed with exception: " + ex.toString());
        }
        LOG.info("File uploaded. File UUID is " + fileUUID);
        return fileUUID;
    }

    public static String uploadAppToBitbarAndGetUUID(String apiKey) {
        LOG.info("Uploading " + APP_PATH + " to Bitbar Cloud");
        String fileUUID;
        try {
            fileUUID = BitbarFileUploader.uploadFile(APP_PATH, APPIUM_URL, apiKey);
        } catch (IOException ex) {
            throw new RuntimeException("API call 'uploadApp' to Bitbar failed with exception: " + ex.toString());
        }
        LOG.info("File uploaded. File UUID is " + fileUUID);
        return fileUUID;
    }
}
