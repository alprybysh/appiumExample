package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alekspribysh on 4/2/19.
 */
public class PerfCpuInfo extends PerfBase{

    static List<String> cpuData = new ArrayList<>();

    public PerfCpuInfo(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/cpuinfo.log");
    }


    public static List<String> getCpuInfo(ShellUtils utils) {
        return utils.getPerfInfoString(PerfType.CPUINFO, "");
    }

    public static void dumpCpuInfo (TestStatus testStatus, ShellUtils utils){
        PerfCpuInfo CpuInfo = new PerfCpuInfo(testStatus);
        cpuData = getCpuInfo(utils);
        CpuInfo.writeToFile();
    }
    @Override
    public String toString() {
        return super.toString() +
                " with following battery performance data: \n" +
                Arrays.toString(cpuData.toArray()) +"\n";
    }
}
