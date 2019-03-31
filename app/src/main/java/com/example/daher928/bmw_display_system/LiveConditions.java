package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
    static double last_point = 0;
    static boolean running;
    private static LineGraphSeries<DataPoint> series1 = null;
    private static LineGraphSeries<DataPoint> series2 = null;
    private static LineGraphSeries<DataPoint> series3 = null;

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
                startActivity(new Intent(LiveConditions.this, ChooseConfig.class));
                last_point = 0;
                running = false;
            }
        });

        //Layout weight - depends on # of selected sensors
        LinearLayout layout = findViewById(R.id.linearLayoutGraph);
        float weight = ((float) AppState.selectedIds.size()) / (float) 4;
        layout.setWeightSum(weight);

        running = true;


        // -------------*** Graphs ***------------------------------------------//
        GraphView graph1 = (GraphView) findViewById(R.id.graph1);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        GraphView graph3 = (GraphView) findViewById(R.id.graph3);

        series1 = new LineGraphSeries<DataPoint>();
        series2 = new LineGraphSeries<DataPoint>();
        series3 = new LineGraphSeries<DataPoint>();

        //Graph #1
        Sensor sensor1 = AppState.getSensorFromId(AppState.selectedIds.get(0));
        configureGraph(graph1,series1,sensor1);

        //Graph #2
        if(AppState.selectedIds.size() > 1){
            Sensor sensor2 = AppState.getSensorFromId(AppState.selectedIds.get(1));
            configureGraph(graph2,series2,sensor2);
        }

        //Graph #3
        if(AppState.selectedIds.size() > 2){
            Sensor sensor3 = AppState.getSensorFromId(AppState.selectedIds.get(2));
            configureGraph(graph3,series3,sensor3);
        }
//        graph1.addSeries(series1);
//        graph1.getViewport().setYAxisBoundsManual(true);
//        graph1.getViewport().setMinY(sensor1.getMinVal());
//        graph1.getViewport().setMaxY(sensor1.getConfig().getMaxY());
//        graph1.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//        graph1.setTitle(sensor1.getName() + " [" + sensor1.getId() + "]");
//        graph1.setTitleTextSize(20);
//        graph1.getGridLabelRenderer().setVerticalAxisTitle(sensor1.getUnits());
//        graph1.getGridLabelRenderer().setVerticalAxisTitleTextSize(15);
//        graph1.getGridLabelRenderer().setHorizontalAxisTitle("Time");
//        graph1.getGridLabelRenderer().setHorizontalAxisTitleTextSize(15);
//        graph1.getGridLabelRenderer().setTextSize(15);
//        graph1.getGridLabelRenderer().setHumanRounding(false);
//        graph1.getGridLabelRenderer().setHighlightZeroLines(false);
//        graph1.getGridLabelRenderer().setLabelsSpace(-10);
//        graph1.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new SimpleDateFormat("mm:ss.SSS")));
//        graph1.getGridLabelRenderer().setLabelVerticalWidth((int)getResources().getDimension(R.dimen.activity_horizontal_margin));
//        graph1.getGridLabelRenderer().setLabelHorizontalHeight((int)getResources().getDimension(R.dimen.activity_vertical_margin));
//        graph1.getGridLabelRenderer().setGridColor(Color.parseColor("#20000000"));
//        series1.setColor(sensor1.getConfig().getColor());
//        series1.setDrawBackground(true);
//        series1.setBackgroundColor(Color.argb(0x3F, 0x47, 0x2c, 0x17));
//        series1.setThickness(6);
//        series1.setDrawDataPoints(true);
//        -------------------------***------------------------------------------//

        Thread SDThread = new StreamDisplayThread();
        SDThread.start();
    }

    private void configureGraph(GraphView graph, LineGraphSeries series, Sensor sensor){
        graph.addSeries(series);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(sensor.getMinVal());
        graph.getViewport().setMaxY(sensor.getConfig().getMaxY());
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.setTitle(sensor.getName() + " [" + sensor.getId() + "]");
        graph.setTitleTextSize(20);
        graph.getGridLabelRenderer().setVerticalAxisTitle(sensor.getUnits());
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(15);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(15);
        graph.getGridLabelRenderer().setTextSize(15);
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setLabelsSpace(-3);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new SimpleDateFormat("mm:ss.SSS")));
        graph.getGridLabelRenderer().setLabelVerticalWidth((int)getResources().getDimension(R.dimen.activity_vertical_margin));
        graph.getGridLabelRenderer().setLabelHorizontalHeight((int)getResources().getDimension(R.dimen.activity_horizontal_margin));
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#20000000"));
        series.setColor(sensor.getConfig().getColor());
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(0x3F, 0x47, 0x2c, 0x17));
        series.setThickness(3);
        series.setDrawDataPoints(true);
    }

    class StreamDisplayThread extends Thread implements Runnable {
        Handler h = new Handler();
        public StreamDisplayThread() {
            last_point = 0;
        }

        @Override
        public void run() {
//            last_point = 0;
            while (running) {
                while (!AppState.queue.isEmpty() && running) {
//                    try {
//                        sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    final String s = AppState.queue.poll();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Timestamp ts = new Timestamp(System.currentTimeMillis());
                            Date d1 = new Date(ts.getTime());
                            if (s == null) {
//                                series1.appendData(new DataPoint(d1, last_point), true, 10, false);
//                                Log.d("s is null - point will be displayed", String.valueOf(last_point));
                                return;
                            }

                            final String id = s.split("#")[0];
                            final String val = s.split("#")[1];

                            if (!AppState.selectedIds.contains(id)){
                                return;
//                                series1.appendData(new DataPoint(d1, last_point), true, 10, false);
//                              textView.append(String.valueOf(last_point) + " " + d1 + "\n");
//                                Log.d("not matching if -  point will be displayed", String.valueOf(last_point));
//                              graph.getViewport().scrollToEnd();


                            } else {
                                final double double_val = Integer.valueOf(val, 16);

                                Log.d("New point will be displayed", s);
                                int graph_idx = AppState.selectedIds.indexOf(id);
                                // Calendar calendar = Calendar.getInstance();
                                Sensor currSensor = AppState.getSensorFromId(AppState.selectedIds.get(graph_idx));
                                double resolution = currSensor.getConfig().getResolution();
                                double offset = currSensor.getOffset();
                                double final_val = double_val * resolution + offset;
                                last_point = final_val;

                                switch(graph_idx){
                                    case 0:
                                        series1.appendData(new DataPoint(d1, final_val), true, 10, false);
                                        break;
                                    case 1:
                                        series2.appendData(new DataPoint(d1, final_val), true, 10, false);
                                        break;
                                    case 2:
                                        series3.appendData(new DataPoint(d1, final_val), true, 10, false);
                                        break;
                                }

                                textView.append(id + " " + String.valueOf(final_val) + " " + d1 + "\n");
//                              graph.getViewport().scrollToEnd();
                                Log.d("New point will be displayed", String.valueOf(final_val));
                            }

                        }
                    });


//                    }


                }
            }
        }
    }
}