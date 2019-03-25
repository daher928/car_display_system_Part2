package com.example.daher928.bmw_display_system;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;

public class CSVReader{


    public static void readSensorsCSV(InputStream is) {
        AppState.sensors_list.clear();
        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";
        // Initialization
        try {

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity","Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                Sensor new_sensor = new Sensor();

                // Setters
                new_sensor.setId(String.valueOf(tokens[0]).toLowerCase());
                new_sensor.setName(tokens[1]);
                new_sensor.setUnits(tokens[2]);
                new_sensor.setMinVal(Double.parseDouble(tokens[3]));
                new_sensor.setMaxVal(Double.parseDouble(tokens[4]));
                new_sensor.setResolution(Double.parseDouble(tokens[5]));
                new_sensor.setOffset(Double.parseDouble(tokens[6]));
                // Adding object to a class
                AppState.sensors_list.add(new_sensor);

                // Log the object
                Log.d("Sensors:", "created: " + new_sensor + " resolution=" + new_sensor.getResolution() + " offset=" + new_sensor.getOffset());
            }
            AppState.sensors_list.sort(new Comparator<Sensor>() {
                @Override
                public int compare(Sensor s1, Sensor s2) {
                    return s1.getId().compareToIgnoreCase(s2.getId());
                }
            });

        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

}
