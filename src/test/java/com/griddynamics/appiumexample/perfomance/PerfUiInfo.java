package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;

import java.util.List;

public class PerfUiInfo extends PerfBase {

    private int totalFrames;
    private int jankyFrames;
    private int percentile50;
    private int percentile90;
    private int persentile95;
    private int persentile99;
    private int missedVsync;
    private int highInputLatency;
    private int slowUiThread;
    private int slowBitmapUploads;
    private int slowIssueDrawCommands;


    public PerfUiInfo(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/gfxinfo.log");
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public void setJankyFrames(int jankyFrames) {
        this.jankyFrames = jankyFrames;
    }

    public void setPercentile50(int percentile50) {
        this.percentile50 = percentile50;
    }

    public void setPercentile90(int percentile90) {
        this.percentile90 = percentile90;
    }

    public void setPersentile95(int persentile95) {
        this.persentile95 = persentile95;
    }

    public void setPersentile99(int persentile99) {
        this.persentile99 = persentile99;
    }

    public void setMissedVsync(int missedVsync) {
        this.missedVsync = missedVsync;
    }

    public void setHighInputLatency(int highInputLatency) {
        this.highInputLatency = highInputLatency;
    }

    public void setSlowUiThread(int slowUiThread) {
        this.slowUiThread = slowUiThread;
    }

    public void setSlowBitmapUploads(int slowBitmapUploads) {
        this.slowBitmapUploads = slowBitmapUploads;
    }

    public void setSlowIssueDrawCommands(int slowIssueDrawCommands) {
        this.slowIssueDrawCommands = slowIssueDrawCommands;
    }

    public static void dumpUiInfo(TestStatus testStatus, ShellUtils utils) {
        PerfUiInfo uiInfo = new PerfUiInfo(testStatus);
        List<Integer> uiNumbers = getUiInfo(utils);
        uiInfo.copyUiInfo(uiNumbers);
        uiInfo.writeToFile();
    }

    public static List<Integer> getUiInfo(ShellUtils utils) {
        return utils.getPerfInfo(PerfType.GFXINFO);
    }

    public void copyUiInfo(List<Integer> uiInfo) {
        setTotalFrames(uiInfo.get(0));
        setJankyFrames(uiInfo.get(1));
        setPercentile50(uiInfo.get(2));
        setPercentile90(uiInfo.get(3));
        setPersentile95(uiInfo.get(4));
        setPersentile99(uiInfo.get(5));
        setMissedVsync(uiInfo.get(6));
        setHighInputLatency(uiInfo.get(7));
        setSlowUiThread(uiInfo.get(8));
        setSlowBitmapUploads(uiInfo.get(9));
        setSlowIssueDrawCommands(uiInfo.get(10));
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public int getJankyFrames() {
        return jankyFrames;
    }

    public int getPercentile50() {
        return percentile50;
    }

    public int getPercentile90() {
        return percentile90;
    }

    public int getPersentile95() {
        return persentile95;
    }

    public int getPersentile99() {
        return persentile99;
    }

    public int getMissedVsync() {
        return missedVsync;
    }

    public int getHighInputLatency() {
        return highInputLatency;
    }

    public int getSlowUiThread() {
        return slowUiThread;
    }

    public int getSlowBitmapUploads() {
        return slowBitmapUploads;
    }

    public int getSlowIssueDrawCommands() {
        return slowIssueDrawCommands;
    }

    @Override
    public String toString() {
        return super.toString() +
                " with following gfxinfo performance data: \n" +
                "Total Frames=" + totalFrames +
                ", Janky Frames=" + jankyFrames +
                ", 50th percentile=" + percentile50 + "ms" +
                ", 90th percentile=" + percentile90 + "ms" +
                ", 95th percentile=" + persentile95 + "ms" +
                ", 99th percentile=" + persentile99 + "ms" +
                ", Number Missed Vsync=" + missedVsync +
                ", Number High input latency=" + highInputLatency +
                ", Number Slow UI thread=" + slowUiThread +
                ", Number Slow bitmap uploads=" + slowBitmapUploads +
                ", Number Slow issue draw commands=" + slowIssueDrawCommands + "\n";
    }

}
