package com.example.daher928.bmw_display_system;

public class Sensor {
    private int id;
    private String name; //optional
    private String units;


    @Override
    public String toString() {
        return  name + " [" + units + "]" ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnits(String units) {
        this.units = units;
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
