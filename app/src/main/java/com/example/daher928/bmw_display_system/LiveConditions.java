package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LiveConditions extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> series;
    int x=5, y=4;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_conditions);

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        graph.addSeries(series);
        Button anotherData = findViewById(R.id.anotherData);
        anotherData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    series.appendData( new DataPoint(x++,y+=2),true, 200);
                    graph.addSeries(series);
                    if(y>8)
                        y=0;
                }

            }
        });



//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);
//        series.appendData( new DataPoint(x,y),false, 200);



    }

}
