package com.griddynamics.appiumexample.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.griddynamics.appiumexample.perfomance.PerfType;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.text.StringEscapeUtils;

import java.text.DateFormat;
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

    public String getAppVersion() {
        Map<String, Object> verArgs = new HashMap<>();
        verArgs.put("command", "dumpsys");
        verArgs.put("args",
                Lists.newArrayList("package", driver.getCurrentPackage(), "| grep versionName"));
        String[] ver = executeCommand(verArgs).split("=");
        return ver[1].replaceAll("\n", "");
    }

    public List<String> getRawDumpsysInfo(PerfType perfType) {
        Map<String, Object> perfArgs = new HashMap<>();
        perfArgs.put("command", "dumpsys");
        perfArgs.put("args", Lists.newArrayList(perfType.getValue(), driver.getCurrentPackage()));
        String result = executeCommand(perfArgs);
        return Arrays.asList(result.split("\n"));
    }

    public List<Integer> getPerfInfo(PerfType perfType)  {
        List<Integer> perfInfo = new ArrayList<>();
        String pattern = "";
        List<String> resList = getRawDumpsysInfo(perfType);
        switch (perfType.getValue()) {
            case "meminfo":
                // https://developer.android.com/studio/command-line/dumpsys#meminfo
                // Takes output strings from 26 to 36 with App summary info (Java Heap - TOTAL)
                resList = resList.subList(26, 36);
                pattern = ParseAdbShell.memPattern;
                break;
            case "gfxinfo":
                // https://developer.android.com/training/testing/performance#aggregate
                // Takes output strings from 6 to 17 with aggregate frame stats
                resList = resList.subList(6, 17);
                pattern = ParseAdbShell.uiPattern;
                resetPerfInfo(perfType);
                break;
            case "cpuinfo":
                System.out.println();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                long requestTime = System.currentTimeMillis();
                pattern = ParseAdbShell.DATEPATTERN;
                List<Date> dates = new ArrayList<>();
                while ((System.currentTimeMillis() - requestTime) < 340_000) {
                    System.out.println();
                    System.out.println("While started   " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                    System.out.println("while length    " + (System.currentTimeMillis() - requestTime));
                    for (String str : resList) {
                        if (str.contains(driver.getCurrentPackage())){
                            System.out.println();
                            System.out.println("###################");
                            System.out.println("I've goat a package");
                            for (String st : resList) {
                                System.out.println(st);
                            }
                            return perfInfo;
                        }
                        if (str.contains("CPU usage from")) {
                            dates = ParseAdbShell.captureDates(str, pattern);
                        }

                    }
                    if (requestTime > dates.get(0).getTime() && requestTime < dates.get(1).getTime()) {
                        System.out.println();
                        System.out.println("&&&&&&&&&&&&&&&&&");
                        System.out.println("I've got a report");
                        System.out.println();
                        for (String str : resList) {
                            System.out.println(str);
                        }
                        break;
                    }
                    else{
                        System.out.println();
                        System.out.println("%%%%%%%%%%%%%%%%%%%%");
                        System.out.println("Current time date     " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                        System.out.println("Report start date     " + dates.get(0));
                        System.out.println("Report finished date  " + dates.get(1));
                        System.out.println("Report finished in ms " + dates.get(1).getTime());
                        System.out.println("Current time in ms    " + System.currentTimeMillis());
                        System.out.println("Wait time             " + (dates.get(1).getTime() + 330_000 - System.currentTimeMillis()));
                        try {
                            Thread.sleep(dates.get(1).getTime() + 330_000 - System.currentTimeMillis());
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        resList = getRawDumpsysInfo(perfType);

                    }


                }
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                return perfInfo;
        }
        for (String str : resList) {
            perfInfo.addAll(ParseAdbShell.captureValues(str, pattern));
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
