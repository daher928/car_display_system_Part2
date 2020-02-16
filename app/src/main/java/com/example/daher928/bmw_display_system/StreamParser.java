package com.example.daher928.bmw_display_system;

public class StreamParser {

    public static StreamLine parse(String streamStr){
        if (streamStr.isEmpty() || streamStr.charAt(0)!=1){
            return null;
        }
        return new StreamLine(streamStr);
    }

}
