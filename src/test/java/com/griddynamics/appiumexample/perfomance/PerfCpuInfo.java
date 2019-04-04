package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.List;

/**
 * Created by alekspribysh on 4/2/19.
 */
public class PerfCpuInfo extends PerfBase{

    public PerfCpuInfo(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/cpuinfo.log");
    }


    public static List<Integer> getCpuInfo(ShellUtils utils) {
        return utils.getPerfInfo(PerfType.CPUINFO);
    }

    public static void dumpCpuInfo (TestStatus testStatus, ShellUtils utils){
        PerfCpuInfo CpuInfo = new PerfCpuInfo(testStatus);
        List<Integer> CpuNumbers = getCpuInfo(utils);
         //uiInfo.copyUiInfo(CpuNumbers);
//        uiInfo.writeToFile();
    }
}
