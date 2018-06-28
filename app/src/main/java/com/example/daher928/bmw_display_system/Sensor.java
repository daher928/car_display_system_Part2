package com.example.daher928.bmw_display_system;

public class Sensor {
    private int id;
    private String name; //optional
    private String units;

    public Sensor(int s_id, String s_name, String s_units){
        id = s_id;
        name = s_name;
        units = s_units;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getUnits(){
        return units;
    }

}
