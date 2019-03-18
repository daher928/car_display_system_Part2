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

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LiveConditions extends AppCompatActivity {

    TextView textView;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private double lastX = 0;

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

        Thread SDThread = new Thread(new StreamDisplayThread());
        SDThread.start();


        // -------------*** Graph demo ***------------------------------------------//
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(5000);
        viewport.setScrollable(true);
        viewport.setScrollableY(true); // enables vertical scrolling

        LinearLayout layout = findViewById(R.id.linearLayoutGraph);
        float weight = ((float)AppState.selectedIds.size())/(float)4;
        layout.setWeightSum(weight);
        // set date label formatter
        graph.getGridLabelRenderer().setNumVerticalLabels(8);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    DateFormat simple = new SimpleDateFormat("HH:mm:ss:SSS");
                    Date result = new Date((long)value);
                    return simple.format(result);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }

        });
//        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        // ------------------------------***------------------------------------------//

    }

    class StreamDisplayThread extends Thread implements Runnable {
        Handler h = new Handler();
        @Override
        public void run() {
            while(true){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(!AppState.queue.isEmpty()) {
                    final String s = AppState.queue.poll();
                    if(s==null)
                        continue;
                    String id = s.split("#")[0];
                    String val = s.split("#")[1];

                    if(!AppState.selectedIds.contains(String.valueOf(id)))
                        continue;
                    final double double_val = Integer.valueOf(val,16);
                    Log.d("StreamDisplayThread polled:" , s);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            lastX += 0.01;
                          // Calendar calendar = Calendar.getInstance();
                            Timestamp ts = new Timestamp(System.currentTimeMillis());
                            Date d1 = new Date(ts.getTime());


                            series.appendData(new DataPoint(d1, double_val), false, 10);
                            textView.append(s +"\n");
                        }
                    });
                }
            }
        }
    }

//
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
}
