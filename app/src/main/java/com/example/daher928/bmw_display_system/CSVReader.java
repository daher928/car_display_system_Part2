package com.example.daher928.bmw_display_system;

import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;

public class CSVReader{

    private static final String DELIMITER = ",";
    private static final String CHARSET_FORMAT = "UTF-8";

    /*
    Reads text from character-input stream, buffering characters for efficient reading
     */
    public static void readSensorsCSV(InputStream is) {
        AppState.sensors_list.clear();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName(CHARSET_FORMAT))
        );

        String line = "";
        try {

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity","Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(DELIMITER);
                // Read the data
                Sensor new_sensor = new Sensor();

                // Setters
                new_sensor.setId(String.valueOf(tokens[0]).toLowerCase());
                new_sensor.setName(tokens[1]);
                new_sensor.setUnits(tokens[2]);
                new_sensor.setMinVal(Double.parseDouble(tokens[3]));
                new_sensor.setInitMaxVal(Double.parseDouble(tokens[4]));
                new_sensor.setInitResolution(Double.parseDouble(tokens[5]));
                new_sensor.setOffset(Double.parseDouble(tokens[6]));
                new_sensor.setConfig(new SensorConfiguration(Color.BLUE,Double.parseDouble(tokens[4]),Double.parseDouble(tokens[5])));
                // Adding object to a class
                AppState.sensors_list.add(new_sensor);

                // Log the object
                Log.d("Sensors:", "created: " + new_sensor + " resolution=" + new_sensor.getInitResolution() + " offset=" + new_sensor.getOffset());
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
