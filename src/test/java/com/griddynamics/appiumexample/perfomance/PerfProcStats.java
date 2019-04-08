package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.List;

/**
 * Created by alekspribysh on 4/4/19.
 */
public class PerfProcStats  extends PerfBase {


    private int minPSS;
    private int avgPSS;
    private int maxPSS;
    private int minUSS;
    private int avgUSS;
    private int maxUSS;


    public void setMinPSS(int minPSS) {
        this.minPSS = minPSS;
    }

    public void setAvgPSS(int avgPSS) {
        this.avgPSS = avgPSS;
    }

    public void setMaxPSS(int maxPSS) {
        this.maxPSS = maxPSS;
    }

    public void setMinUSS(int minUSS) {
        this.minUSS = minUSS;
    }

    public void setAvgUSS(int avgUSS) {
        this.avgUSS = avgUSS;
    }

    public void setMaxUSS(int maxUSS) {
        this.maxUSS = maxUSS;
    }

    public int getMinPSS() {
        return minPSS;
    }

    public int getAvgPSS() {
        return avgPSS;
    }

    public int getMaxPSS() {
        return maxPSS;
    }

    public int getMinUSS() {
        return minUSS;
    }

    public int getAvgUSS() {
        return avgUSS;
    }

    public int getMaxUSS() {
        return maxUSS;
    }



    public PerfProcStats(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/procstat.log");
    }


    public static List<Integer> getProcStats(ShellUtils utils) {
        return utils.getPerfInfo(PerfType.PROCSTATS, "--hours 1");
    }

    public static String cleanProcStats(ShellUtils utils){
        return utils.clearPerfStats(PerfType.PROCSTATS, "--clear");
    }

    public static void dumpProcStatsInfo (TestStatus testStatus, ShellUtils utils){

        PerfProcStats procStatsInfo = new PerfProcStats(testStatus);

        List<Integer> procStatsnumbers = getProcStats(utils);
        procStatsInfo.copyProcStatsInfo(procStatsnumbers);
        procStatsInfo.writeToFile();
    }

    public void copyProcStatsInfo(List<Integer> procStatInfo) {

        setMinPSS(procStatInfo.get(1));
        setAvgPSS(procStatInfo.get(2));
        setMaxPSS(procStatInfo.get(3));
        setMinUSS(procStatInfo.get(4));
        setAvgUSS(procStatInfo.get(5));
        setMaxUSS(procStatInfo.get(6));

    }
    @Override
    public String toString() {
        return super.toString() +
                " with following procstats performance data: \n" +
                " Min PSS = " + minPSS + ",\n" +
                " Avg PSS = " + avgPSS + ",\n" +
                " Max PSS = " + maxPSS + ",\n" +
                " Min USS = " + minUSS + ",\n" +
                " Avg USS = " + avgUSS + ",\n" +
                " Max USS = " + maxUSS + ",\n";
    }

}
