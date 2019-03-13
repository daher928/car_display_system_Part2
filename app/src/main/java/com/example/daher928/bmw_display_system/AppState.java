package com.example.daher928.bmw_display_system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;

public class AppState {

    public static ArrayList<Sensor> sensors_list = new ArrayList<>();
    static List<String> selectedDiNamesList = new ArrayList<>();
    static List<String> selectedIds = new ArrayList<>();

    static final String LIST_GRID = "list_grid";
    static final String BLOCKS_GRID = "blocks_grid";
    static final String SLIDES_GRID = "slides_grid";

    static String gridSelected = null;

    static List<String> receivedData = new ArrayList<>();
    static Queue<String> queue = new LinkedTransferQueue<>();

    public static String[] getSensorsDiNames() {   //diName = Name [Units] (ID=XXXX)
        Iterator<Sensor> iterator = AppState.sensors_list.iterator();
        ArrayList<String> diNamesList = new ArrayList<>();

        while (iterator.hasNext()) {
            diNamesList.add(iterator.next().toString());
        }
        return  diNamesList.toArray(new String[0]);
    }

    public static String[] getSensorsIds() {   //ids
        Iterator<Sensor> iterator = AppState.sensors_list.iterator();
        ArrayList<String> idsList = new ArrayList<>();

        while (iterator.hasNext()) {
            idsList.add(iterator.next().getId());
        }
        return  idsList.toArray(new String[0]);
    }

    public static String getSensorIdFromDiName(String diName){
        return (diName.split("=")[1]).split("\\)")[0];
    }
}
