package com.example.daher928.bmw_display_system;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SensorConfiguration {

    private int color;
    private double maxY;
    private double resolution;

    public SensorConfiguration(int color, double maxY, double resolution) {
        this.color = color;
        this.maxY = maxY;
        this.resolution = resolution;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getResolution() {
        return resolution;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    private String colorToString(int color){
        String str = "";
        Log.i(TAG, "color " + color);
        switch (color){
            case Color.BLUE:
                str = "Blue";
                break;
            case Color.GREEN:
                str = "Green";
                break;
            case Color.BLACK:
                str = "Black";
                break;
            case Color.YELLOW:
                str = "Yellow";
                break;
            case Color.RED:
                str = "Red";
                break;
        }
        return str;
    }

    public Map<String, Object> sensorConfigMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("maxY", maxY);
        map.put("color", color);
        map.put("resolution", resolution);
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + colorToString(color) + " MaxY:" + maxY + " Res:" + resolution + "]";
    }
}
