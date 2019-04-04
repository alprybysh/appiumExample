package com.griddynamics.appiumexample.utils.kobiton;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KobitonListDevices {
    public List<KobitonDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<KobitonDevice> devices) {
        this.devices = devices;
    }

    @JsonProperty("cloudDevices")
    private List<KobitonDevice> devices = new ArrayList<>();
}
