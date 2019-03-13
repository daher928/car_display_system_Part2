package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LiveConditions extends AppCompatActivity {
    static int port = 9191;
    TextView textView;
    static List<String> received = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_conditions);

        View v = getWindow().getDecorView();
        final ImageButton back_button = findViewById(R.id.back_button);

        if (AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));
        } else {
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button.animate();
                startActivity(new Intent(LiveConditions.this, ChooseGrid.class));
            }
        });

        textView = findViewById(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

    }

    class MyServerThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        Handler h = new Handler();

        String message;

        @Override
        public void run(){
            try{
                ss = new ServerSocket(port);
                while(true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    while((message = br.readLine()) != null){
                        received.add(message);
                        Log.i("received:" , message);
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.append(message + "\n");
                            }
                        });
                    }

                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
//
//    private static final Random RANDOM = new Random();
//    private LineGraphSeries<DataPoint> series;
//
//
//    private double lastX = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_live_conditions);
//        // we get graph view instance
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        // data
//        series = new LineGraphSeries<DataPoint>();
//        graph.addSeries(series);
//        // customize a little bit viewport
//        Viewport viewport = graph.getViewport();
//        viewport.setYAxisBoundsManual(true);
//        viewport.setMinY(0);
//        viewport.setMaxY(10);
//        viewport.setScrollable(true);
//        viewport.setScrollableY(true); // enables vertical scrolling
//
//
//        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
//        // data
//        graph2.addSeries(series);
//        // customize a little bit viewport
//        Viewport viewport2 = graph2.getViewport();
//        viewport2.setYAxisBoundsManual(true);
//        viewport2.setMinY(0);
//        viewport2.setMaxY(10);
//        viewport2.setScrollable(true);
//        viewport2.setScrollableY(true); // enables vertical scrolling
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // we're going to simulate real time with thread that append data to the graph
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while(true){
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            addEntry();
//                        }
//                    });
//
//                    // sleep to slow down the add of entries
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        // manage error ...
//                    }
//                }
//            }
//        }).start();
//    }
//
//    // add random data to graph
//    private void addEntry() {
//        // here, we choose to display max 10 points on the viewport and we scroll to end
//        lastX += 0.01;
//        series.appendData(new DataPoint(lastX, RANDOM.nextDouble() * 10d), false, 10);
//    }
//


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
//}
