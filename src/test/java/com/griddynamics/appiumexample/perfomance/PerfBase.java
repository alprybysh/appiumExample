package com.griddynamics.appiumexample.perfomance;

import com.griddynamics.appiumexample.TestStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class PerfBase {
    private String testName;
    private String device;
    private String date;
    private String appVer;
    private String status;
    private String pathToFile;

    public PerfBase(TestStatus testStatus) {
        testName = testStatus.getMethodName();
        status = testStatus.getStatus();
        device = testStatus.getDeviceName();
        date = testStatus.getDate();
        appVer = testStatus.getAppVersion();
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = TestStatus.getExecutionStatus(status);
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getTestName() {
        return testName;
    }

    public String getDevice() {
        return device;
    }

    public String getDate() {
        return date;
    }

    public String getAppVer() {
        return appVer;
    }

    public String getStatus() {
        return status;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void writeToFile() {
        String path = Objects.requireNonNull(getPathToFile(), "Path to file is not set!");
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(path, true))) {
            printWriter.println(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Test method '" + testName + '\'' + " "
                + status +
                " on device '" + device + '\'' +
                " and app version='" + appVer + '\'' +
                " at '" + date + '\'';
    }
}