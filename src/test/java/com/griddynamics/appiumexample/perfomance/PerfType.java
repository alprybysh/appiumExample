package com.griddynamics.appiumexample.perfomance;

public enum PerfType {

    MEMINFO("meminfo"),
    GFXINFO("gfxinfo"),
    CPUINFO("cpuinfo"),
    BATTERYSTATS("batterystats");

    private final String value;

    PerfType(String value) {
        this.value  = value;
    }

    public String getValue(){
        return this.value;
    }
}
