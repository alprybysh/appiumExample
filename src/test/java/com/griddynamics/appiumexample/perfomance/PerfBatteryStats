package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alekspribysh on 4/5/19.
 */
public class PerfBatteryStats extends PerfBase {

    private static List<String> batteryData = new ArrayList<>();

    public PerfBatteryStats(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/batterystats.log");
    }

    public static List<String> getBatteryStats(ShellUtils utils) {
        return utils.getPerfInfoString(PerfType.BATTERYSTATS, "");
    }

    public static String cleanBatteryStats(ShellUtils utils) {
        return utils.clearPerfStats(PerfType.BATTERYSTATS, "--reset");
    }

    public static void dumpBatteryStats(TestStatus testStatus, ShellUtils utils) {

        PerfBatteryStats perfBatteryInfo = new PerfBatteryStats(testStatus);
        batteryData = getBatteryStats(utils);
        for (String str : batteryData) {
            System.out.println(str);
        }
        perfBatteryInfo.writeToFile();
    }
    @Override
    public String toString() {
        return super.toString() +
                " with following battery performance data: \n" +
                Arrays.toString(batteryData.toArray()) +"\n";
    }
}
