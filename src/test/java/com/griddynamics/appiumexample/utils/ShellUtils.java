package com.griddynamics.appiumexample.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.griddynamics.appiumexample.perfomance.PerfType;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;


public class ShellUtils {

    private static final String APP_DATA_PATH = "/data/data/com.example.sharingapp/files";
    private static final String ITEMS_FILE = "items.sav";
    private AndroidDriver driver;

    public ShellUtils(AndroidDriver driver) {
        this.driver = driver;
    }

    private String getFileItemsPath() {
        return String.format("%s/%s", APP_DATA_PATH, ITEMS_FILE);
    }

    private String executeCommand(Map<String, Object> params) {
        return (String) driver.executeScript("mobile:shell", params);
    }

    private String createNewItemJson(String title, String maker, String description, int[] hlw) {
        Map<String, String> dimensions = new HashMap<>();
        String[] hlwKeys = {"height", "length", "width"};
        for (int i = 0; i < hlwKeys.length; i++) {
            dimensions.put(hlwKeys[i], String.valueOf(hlw[i]));
        }

        Map<String, Object> item = new HashMap<>();
        Gson gson = new Gson();
        item.put("title", title);
        item.put("maker", maker);
        item.put("description", description);
        item.put("dimensions", dimensions);
        item.put("status", "Available");
        item.put("id", UUID.randomUUID().toString());
        String escapedJson = gson.toJson(Collections.singletonList(item).toArray());
        return StringEscapeUtils.unescapeJson(escapedJson).replace(",", ", ");
    }

    public void putNewItem(String title, String maker, String description, int[] hlw) {
        String itemJson = createNewItemJson(title, maker, description, hlw);
        Map<String, Object> catItems = ImmutableMap.of("command",
                String.format("echo %s > ", itemJson), "args", getFileItemsPath());
        executeCommand(catItems);
    }

    public String clearPerfStats(PerfType perfType, String options){
        Map<String, Object> perArgs = new HashMap<>();
        perArgs.put("command", "dumpsys");
        perArgs.put("args", Lists.newArrayList(perfType.getValue(), options));
     return executeCommand(perArgs);
    }

    public String getAppVersion() {
        Map<String, Object> verArgs = new HashMap<>();
        verArgs.put("command", "dumpsys");
        verArgs.put("args",
                Lists.newArrayList("package", driver.getCurrentPackage(), "| grep versionName"));
        String[] ver = executeCommand(verArgs).split("=");
        return ver[1].replaceAll("\n", "");
    }

    public List<String> getRawDumpsysInfo(PerfType perfType, String perfoptions) {
        Map<String, Object> perfArgs = new HashMap<>();
        perfArgs.put("command", "dumpsys");
        perfArgs.put("args", Lists.newArrayList(perfType.getValue(), perfoptions, driver.getCurrentPackage()));
        String result = executeCommand(perfArgs);
        return Arrays.asList(result.split("\n"));
    }

    public List<Integer> getPerfInfo(PerfType perfType, String perfoptions)  {
        List<Integer> perfInfo = new ArrayList<>();
        String pattern = "";
        List<String> resList = getRawDumpsysInfo(perfType, perfoptions);
        switch (perfType.getValue()) {
            case "meminfo":
                // https://developer.android.com/studio/command-line/dumpsys#meminfo
                // Takes output strings from 26 to 36 with App summary info (Java Heap - TOTAL)
                resList = resList.subList(26, 36);
                pattern = ParseAdbShell.generalPattern;
                break;
            case "gfxinfo":
                // https://developer.android.com/training/testing/performance#aggregate
                // Takes output strings from 6 to 17 with aggregate frame stats
                resList = resList.subList(6, 17);
                pattern = ParseAdbShell.uiPattern;
                resetPerfInfo(perfType);
                break;
            case "procstats":
                // https://developer.android.com/studio/command-line/dumpsys#procstats
                // Takes output strings from 9 to 10 with aggregate memory stats
                resList = resList.subList(9, 10);
                pattern = ParseAdbShell.generalPattern;
                break;

        }
        for (String str : resList) {
            perfInfo.addAll(ParseAdbShell.captureValues(str, pattern));
        }
        return perfInfo;

    }

    public List<String> getPerfInfoString(PerfType perfType, String perfoptions)  {
        List<String> perfInfo = new ArrayList<>();
        List<String> resList = getRawDumpsysInfo(perfType, perfoptions);
        switch (perfType.getValue()) {
            case "batterystats":
                // https://developer.android.com/studio/command-line/dumpsys#battery
                perfInfo = resList;
                break;
            case "cpuinfo":
                // Takes output strings from 0 to 8 with cpu info
                perfInfo = resList.subList(0, 8);
                break;
        }
        return perfInfo;

    }

    private void resetPerfInfo(PerfType perfType) {
        Map<String, Object> resetPerfArgs = new HashMap<>();
        resetPerfArgs.put("command", "dumpsys");
        resetPerfArgs
                .put("args", Lists.newArrayList(perfType.getValue(), driver.getCurrentPackage(), " reset"));
        executeCommand(resetPerfArgs);
    }
}
