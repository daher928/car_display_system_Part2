package com.example.daher928.bmw_display_system;

import android.graphics.Color;

public class Sensor {
    private String id;
    private String name; //optional
    private String units;
    private double minVal;
    private double initMaxVal;
    private double initResolution;
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

    public double getInitMaxVal() {
        return initMaxVal;
    }

    public void setInitMaxVal(double initMaxVal) {
        this.initMaxVal = initMaxVal;
    }

    public double getInitResolution() {
        return initResolution;
    }

    public void setInitResolution(double initResolution) {
        this.initResolution = initResolution;
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

    public void resetConfig(){
        config.setResolution(initResolution);
        config.setMaxY(initMaxVal);
        config.setColor(Color.BLUE);
    }


}
