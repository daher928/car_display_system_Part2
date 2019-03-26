package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveConditions extends AppCompatActivity {

    static TextView textView;
    //    static Thread SDThread = null;
    static GraphView graph = null;
    private static LineGraphSeries<DataPoint> series = null;

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
                startActivity(new Intent(LiveConditions.this, ChooseConfig.class));
            }
        });

        textView = findViewById(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod());

//        if (SDThread == null) {
        Thread SDThread = new Thread(new StreamDisplayThread());
        SDThread.start();
//        }


        // -------------*** Graph demo ***------------------------------------------//
        //Layout weight - depends on # of selected sensors
        LinearLayout layout = findViewById(R.id.linearLayoutGraph);
        float weight = ((float) AppState.selectedIds.size()) / (float) 4;
        layout.setWeightSum(weight);

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);

        Sensor sensor1 = AppState.getSensorFromId(AppState.selectedIds.get(0));

        // customize a little bit viewport
        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(sensor1.getMinVal());
        graph.getViewport().setMaxY(sensor1.getConfig().getMaxY());
//        graph.getViewport().setMaxXAxisSize(4);
//        graph.getViewport().setScrollable(true); // enables horizontal scrolling
//        graph.getViewport().setScrollableY(true); // enables vertical scrolling
//        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


//        graph.getViewport().setScrollableY(true);
//        graph.getViewport().setScalableY(true);
        // Configuration of sensor
        graph.setTitle(sensor1.getName() + " [" + sensor1.getId() + "]");
        series.setColor(sensor1.getConfig().getColor());

        // set date label formatter
//        graph.getGridLabelRenderer().setNumVerticalLabels(10);
        graph.getGridLabelRenderer().setNumHorizontalLabels(10);
        graph.getGridLabelRenderer().setLabelsSpace(1);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new SimpleDateFormat("mm:ss.SSS")));
//        -------------------------***------------------------------------------//
        graph.getGridLabelRenderer()
                .setHighlightZeroLines(false); //Otherwise a black ugly line appears for 0-point plot
//        graph.getGridLabelRenderer()
//                .setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
//        graph.getGridLabelRenderer()
//                .setNumVerticalLabels(15);
//        graph.getGridLabelRenderer()
//                .setPadding(50);
        graph.getGridLabelRenderer()
                .setNumHorizontalLabels(20);
//        graph.getGridLabelRenderer()
//                .setPadding(100);
    }

    class StreamDisplayThread extends Thread implements Runnable {
        Handler h = new Handler();
        double last_point = 0;

        @Override
        public void run() {
            last_point = 0;
            while (true) {

                while (!AppState.queue.isEmpty()) {
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final String s = AppState.queue.poll();


                    h.post(new Runnable() {
                        @Override
                        public void run() {
                             Timestamp ts = new Timestamp(System.currentTimeMillis());
                             Date d1 = new Date(ts.getTime());
                            if (s == null) {
                                series.appendData(new DataPoint(d1, last_point), false, 60, false);
                                Log.d("s is null - point will be displayed", String.valueOf(last_point));
                                return;
                            }

                            final String id = s.split("#")[0];
                            final String val = s.split("#")[1];

                            if (!AppState.selectedIds.contains(String.valueOf(id))) {


                                series.appendData(new DataPoint(d1, last_point), false, 60, false);
//                                textView.append(String.valueOf(last_point) + " " + d1 + "\n");
                                Log.d("not matching if -  point will be displayed", String.valueOf(last_point));
                                graph.getViewport().scrollToEnd();

                            } else {
                                final double double_val = Integer.valueOf(val, 16);

                                Log.d("New point will be displayed", s);

//                        h.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                // Calendar calendar = Calendar.getInstance();
                                Sensor currSensor = AppState.getSensorFromId(AppState.selectedIds.get(0));
                                double resolution = currSensor.getConfig().getResolution();
                                double offset = currSensor.getOffset();
                                double final_val = double_val * resolution + offset;
                                last_point = final_val;
                                series.appendData(new DataPoint(d1, final_val), false, 60, false);
                                textView.append(String.valueOf(final_val) + " " + d1 + "\n");
                                graph.getViewport().scrollToEnd();
                                Log.d("New point will be displayed", String.valueOf(final_val));
                            }
//                        },300);
                        }
                    });


//                    }


                }
            }
        }
    }
}