package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alekspribysh on 4/5/19.
 */
public class PerfBatteryStat extends PerfBase {

    private static List<String> batteryData = new ArrayList<>();

    public PerfBatteryStat(TestStatus testStatus) {
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

        PerfBatteryStat perfBatteryInfo = new PerfBatteryStat(testStatus);
        batteryData = getBatteryStats(utils);
        perfBatteryInfo.writeToFile();
    }


    @Override
    public String toString() {
        return super.toString() +
                " with following battery performance data: \n" +
                String.join("\n", batteryData);

    }
}
