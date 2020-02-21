package com.example.daher928.bmw_display_system;

/*
    In Part 2 of the project, a stream line has the following structure:
        1002A1027102710271027
        XYYYYZZZZ...Z
        - X must be 1 (otherwise ignore)
        - YYYY 4 Hexadecimal digits, for Sensor ID
        - ZZZZ...Z Hexadecimal digits, for Sensor value
 */
public class StreamLine {

    public long timeStamp;
    public String sensorId;
    public String sensorData;

    public StreamLine(String stream) {
        this.timeStamp = System.currentTimeMillis();
        this.sensorId = stream.substring(1, 5).toLowerCase();
        this.sensorData = stream.substring(5);
    }

    @Override
    public String toString() {
        return timeStamp + "#" + sensorId + "#" + sensorData;
    }
}
