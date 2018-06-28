package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class LiveConditions extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;


    private double lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_conditions);
        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
        viewport.setScrollableY(true); // enables vertical scrolling

    }

    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        lastX += 0.01;
        series.appendData(new DataPoint(lastX, RANDOM.nextDouble() * 10d), false, 10);
    }



//
//    private LineGraphSeries<DataPoint> series;
//    private int last_x = 0;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_live_conditions);
//
//        //the graph instance
//        GraphView graph = findViewById(R.id.graph);
//        //point series for the graph
//        series = new LineGraphSeries<DataPoint>();
//        //link the series to the graph
//        graph.addSeries(series);
//        //customize viewPort
//        Viewport viewport = graph.getViewport();
//        viewport.setXAxisBoundsManual(true);
//        viewport.setMinY(0);
//        viewport.setMaxY(10);
//        viewport.setScrollable(true);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }
//
//    //adding data to graph
//    private void addEntry(){
//        Random rand = new Random();
//        // we add the point (last_x++, rand) to graph, and display last max 10 points on viewport
//        series.appendData(new DataPoint(last_x++, rand.nextDouble()), true, 10);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "ON RESUME", Toast.LENGTH_LONG);
//    }
}
