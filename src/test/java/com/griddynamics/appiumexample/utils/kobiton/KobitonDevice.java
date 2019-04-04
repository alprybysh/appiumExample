package com.griddynamics.appiumexample.utils.kobiton;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KobitonDevice {
    private String deviceName;
    private String isBooked;
    private String isHidden;
    private String isOnline;
    private String platformName;
    private String platformVersion;

    @JsonSetter("deviceName")
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    @JsonSetter("isBooked")
    public void setIsBooked(String isBooked){
        this.isBooked = isBooked;
    }

    @JsonSetter("isHidden")
    public void setIsHidden(String isHidden){
        this.isHidden = isHidden;
    }

    @JsonSetter("isOnline")
    public void setIsOnline(String isOnline){
        this.isOnline = isOnline;
    }

    @JsonSetter("platformVersion")
    public void setPlatformVersion(String platformVersion){
        this.platformVersion = platformVersion;
    }

    @JsonSetter("platformName")
    public void setPlatformName(String platformName){
        this.platformName = platformName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIsBooked() {
        return isBooked;
    }

    public String getIsHidden() {
        return isHidden;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }
}
