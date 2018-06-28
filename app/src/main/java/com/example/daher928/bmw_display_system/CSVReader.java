package com.example.daher928.bmw_display_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVReader{

    public static ArrayList<Sensor> readCSV(String fileName) throws FileNotFoundException {
        //Get scanner instance
        Scanner scanner = new Scanner(new File(fileName));

        //Set the delimiter used in file
        scanner.useDelimiter(",");

        //Get all tokens and store them in some data structure
        ArrayList<Sensor> sensors_list = new ArrayList<>();
        int sensor_id;
        String sensor_name, sensor_units;
        while (scanner.hasNext())
        {
            sensor_id = scanner.nextInt();
            sensor_name = scanner.next();
            sensor_units = scanner.next();
            sensors_list.add(new Sensor(sensor_id, sensor_name, sensor_units));
        }

        //Do not forget to close the scanner
        scanner.close();
        return sensors_list;
    }
}
