package com.griddynamics.appiumexample;

import java.io.File;
import java.util.Optional;

public class Constants {
    public static final String APPIUM_URL = Optional.ofNullable(System.getProperty("appiumUrl"))
            .orElseThrow(() -> new RuntimeException("Appium URL is not set!"));
    public static final String RUN_TYPE = Optional.ofNullable(System.getProperty("runType"))
            .orElseThrow(()-> new RuntimeException("runType is not set!"));
    public static final String TEST_APP = "sharingApp.apk";
    public static final String ANDROID_PLATFORM_NAME = "Android";
    public static final String IOS_PLATFORM_NAME = "IOS";
    public static final int LOCAL_PORT = 4768;
    public static final String TOTAL_MEMORY = System.getProperty("totalMemory");
    public static final String THRESHOLD = System.getProperty("threshold");

    public static final String APP_PATH = getTargetAppPath();

    private static String getTargetAppPath() {
        File app = new File(Configuration.class.getClassLoader().getResource(TEST_APP).getFile());
        return app.getAbsolutePath();
    }
}
