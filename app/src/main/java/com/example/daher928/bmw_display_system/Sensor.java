package com.example.daher928.bmw_display_system;

public class Sensor {
    private String id;
    private String name; //optional
    private String units;
    private double minVal;
    private double maxVal;
    private double resolution;
    private double offset;
    private SensorConfiguration config;

    @Override
    public String toString() {
        return  name + " [" + units + "]" + " (ID=" + id + ")" ;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getId(){

        return id;
    }

    public String getName(){

        return name;
    }

    public String getUnits(){

        return units;
    }

    public double getMinVal() {
        return minVal;
    }

    public void setMinVal(double minVal) {
        this.minVal = minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public SensorConfiguration getConfig() {
        return config;
    }

    public void setConfig(SensorConfiguration config) {
        this.config = config;
    }


}
