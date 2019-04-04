package com.griddynamics.appiumexample;

import org.testng.ITestResult;
import java.text.DateFormat;
import java.util.Calendar;

public class TestStatus {
    private String methodName;
    private String status;
    private String deviceName;
    private String date;
    private String appVersion;

    public TestStatus(ITestResult testResult, String deviceName, String appVersion) {
        this.methodName = testResult.getMethod().getMethodName();
        this.status = getExecutionStatus(testResult);
        this.deviceName = deviceName;
        this.date = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        this.appVersion = appVersion;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public static String getExecutionStatus(ITestResult testResult){
        return getExecutionStatus(testResult.getStatus());
    }

    public static String getExecutionStatus(int testResult) {
        String status="undefined";
        switch (testResult) {
            case ITestResult.SUCCESS:
                status = "passed";
                break;
            case ITestResult.FAILURE:
                status = "failed";
                break;
            case ITestResult.SKIP:
                status = "skipped";
                break;

        }
        return status;
    }
}
