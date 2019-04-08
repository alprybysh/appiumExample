package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;
import com.griddynamics.appiumexample.utils.ShellUtils;
import org.testng.ITestResult;
import java.util.List;
import static com.griddynamics.appiumexample.Configuration.isMemoryDumpEnabled;
import static com.griddynamics.appiumexample.Constants.TOTAL_MEMORY;
import static com.griddynamics.appiumexample.Constants.THRESHOLD;

public class PerfMemInfo extends PerfBase {

    private int javaHeap;
    private int nativeHeap;
    private int code;
    private int stack;
    private int graphics;
    private int privateOther;
    private int system;
    private int total;

    public PerfMemInfo(TestStatus testStatus) {
        super(testStatus);
        setPathToFile("target/meminfo.log");
    }

    public void setJavaHeap(int javaHeap) {
        this.javaHeap = javaHeap;
    }

    public void setNativeHeap(int nativeHeap) {
        this.nativeHeap = nativeHeap;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

    public void setGraphics(int graphics) {
        this.graphics = graphics;
    }

    public void setPrivateOther(int privateOther) {
        this.privateOther = privateOther;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static void dumpMemInfo(TestStatus testStatus, ShellUtils utils) {
        if (isMemoryDumpEnabled()) {
            PerfMemInfo memInfo = new PerfMemInfo(testStatus);
            List<Integer> memoryNumbers = getMemInfo(utils);
            memInfo.copyMemInfo(memoryNumbers);
            if (!memInfo.memoryThresholdCheck()) {
                memInfo.setStatus(ITestResult.FAILURE);
            }
            memInfo.writeToFile();
        }
    }

    public static List<Integer> getMemInfo(ShellUtils utils) {
        return utils.getPerfInfo(PerfType.MEMINFO, "");
    }

    public void copyMemInfo(List<Integer> memInfo) {
        setJavaHeap(memInfo.get(0));
        setNativeHeap(memInfo.get(1));
        setCode(memInfo.get(2));
        setStack(memInfo.get(3));
        setGraphics(memInfo.get(4));
        setPrivateOther(memInfo.get(5));
        setSystem(memInfo.get(6));
        setTotal(memInfo.get(7));
    }

    public int getJavaHeap() {
        return javaHeap;
    }

    public int getNativeHeap() {
        return nativeHeap;
    }

    public int getCode() {
        return code;
    }

    public int getStack() {
        return stack;
    }

    public int getGraphics() {
        return graphics;
    }

    public int getPrivateOther() {
        return privateOther;
    }

    public int getSystem() {
        return system;
    }

    public int getTotal() {
        return total;
    }

    public boolean memoryThresholdCheck() {
        double totalMemoryBaseline = Double.parseDouble(TOTAL_MEMORY);
        double thresholdPercent = Double.parseDouble(THRESHOLD);
        double totalMemoryInMb = (double) (getTotal()) / 1024;
        double thresholdPercentInMb = totalMemoryInMb * thresholdPercent / 100;
        double thresholdPlus = totalMemoryInMb + thresholdPercentInMb;
        double thresholdMinus = totalMemoryInMb - thresholdPercentInMb;
        return thresholdPlus >= totalMemoryBaseline && thresholdMinus <= totalMemoryBaseline;
    }

    @Override
    public String toString() {
        return super.toString() +
                " with following meminfo performance data (KB): \n" +
                "Java Heap=" + javaHeap +
                ", Native Heap=" + nativeHeap +
                ", Code=" + code +
                ", Stack=" + stack +
                ", Graphics=" + graphics +
                ", Private Other=" + privateOther +
                ", System=" + system +
                ", TOTAL=" + total + "\n";

    }
}