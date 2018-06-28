package com.example.daher928.bmw_display_system;

import java.util.ArrayList;

public class AppState {
    public static ArrayList<Sensor> sensors_list = new ArrayList<>();

    public static void addSensor(int id, String name, String units){
        sensors_list.add(new Sensor(id,name,units));
    }

}
