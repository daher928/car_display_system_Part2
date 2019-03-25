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
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveConditions extends AppCompatActivity {

    static TextView textView;
    static Thread SDThread = null;
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

        if (SDThread == null) {
            SDThread = new Thread(new StreamDisplayThread());
            SDThread.start();
        }


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
        graph.getViewport().setMinY(sensor1.getMinVal());
        graph.getViewport().setMaxY(sensor1.getMaxVal());
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        // Configuration of sensor
        graph.setTitle(sensor1.getName() + " ["+sensor1.getId()+"]");
        series.setColor(Color.GREEN);

        // set date label formatter
        graph.getGridLabelRenderer().setNumVerticalLabels(10);
        graph.getGridLabelRenderer().setNumHorizontalLabels(10);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, new SimpleDateFormat("mm:ss")));
//        -------------------------***------------------------------------------//

    }

    class StreamDisplayThread extends Thread implements Runnable {
        Handler h = new Handler();

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!AppState.queue.isEmpty()) {
                    final String s = AppState.queue.poll();
                    if (s == null)
                        continue;
                    String id = s.split("#")[0];
                    String val = s.split("#")[1];

                    if (!AppState.selectedIds.contains(String.valueOf(id)))
                        continue;

                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    final Date d1 = new Date(ts.getTime());
                    final double double_val = Integer.valueOf(val, 16);

                    Log.d("StreamDisplayThread polled:", s);

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            // Calendar calendar = Calendar.getInstance();
                            Sensor currSensor = AppState.getSensorFromId(AppState.selectedIds.get(0));
                            double resolution = currSensor.getResolution();
                            double offset = currSensor.getOffset();
                            double final_val = double_val * resolution + offset;
                            series.appendData(new DataPoint(d1, final_val), true, 10, false);
                            textView.append(s + "\n");
                        }
                    });
                }
            }
        }
    }
}