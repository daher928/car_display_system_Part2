package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogStats extends AppCompatActivity {

    static TextView completeLogView;
    private FirebaseFirestore firestore;

    public final String ALL_SENSORS_COLLECTION_NAME = "all_sensors";
    public final String SENSOR_ID_DOCUMENT_PROPERTY = "sensor_id";
    public final String SENSOR_DATA_DOCUMENT_PROPERTY = "sensor_data";
    public final String DATE_DOCUMENT_PROPERTY = "timestamp";
    public final String LOG_COLLECTION_NAME = "bmwLog";

//    public final String LOG_FILE_NAME = "bmwLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_stats);

        View v = getWindow().getDecorView();
        ImageButton back_button2 = findViewById(R.id.back_button2);

        if(AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            back_button2.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));
        }else{
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            back_button2.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));
        }

        back_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                startActivity(new Intent(LogStats.this, MainMenu.class));

            }

        });

        firestore = FirebaseFirestore.getInstance();

        completeLogView = findViewById(R.id.completeLogTextView);
        completeLogView.setMovementMethod(new ScrollingMovementMethod());

        Button refreshButton = findViewById(R.id.refreshButtonShadow);
        final Switch filterSwitch = findViewById(R.id.filterSwitch);

        readLog(false);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readLog(filterSwitch.isChecked());
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    readLog(true);
                }else{
                    readLog(false);
                }
            }
        });

    }

    void readLog(boolean isFiltered){
        if (isFiltered && AppState.selectedIds.size()==0) {
            return;
        }

        completeLogView.setText("");
//            FileInputStream fileInputStream = openFileInput(LOG_FILE_NAME);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuffer stringBuffer = new StringBuffer();

//            String lines;
//            while ((lines = bufferedReader.readLine()) != null) {
//                String[] tokens2 = lines.split(" ");
//                String stream = tokens2[tokens2.length-1];
//                StreamLine streamLine = StreamUtil.parse(stream);
//                if (isFiltered){
//                    if(AppState.selectedIds.contains(streamLine.sensorId))
//                        completeLogView.append(lines + "\n");
//                } else {
//                    completeLogView.append(lines + "\n");
//                }
//            }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        if (isFiltered){
            firestore.collection(LOG_COLLECTION_NAME)
                .whereIn(SENSOR_ID_DOCUMENT_PROPERTY, AppState.selectedIds)
                .orderBy(DATE_DOCUMENT_PROPERTY, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                task1.getResult().getDocuments().forEach(
                                        doc -> completeLogView.append(
                                                doc.get(DATE_DOCUMENT_PROPERTY) + " ID=" +
                                                        doc.get(SENSOR_ID_DOCUMENT_PROPERTY) + " Value=" +
                                                        doc.get(SENSOR_DATA_DOCUMENT_PROPERTY) + "\n"));
                            } else {
                                Log.d("ERROR", "get failed with ", task1.getException());
                            }
                        }
                );
        } else {
            firestore.collection(LOG_COLLECTION_NAME)
                    .orderBy(DATE_DOCUMENT_PROPERTY, Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    task1.getResult().getDocuments().forEach(
                                            doc -> completeLogView.append(
                                                    doc.get(DATE_DOCUMENT_PROPERTY) + " ID=" +
                                                            doc.get(SENSOR_ID_DOCUMENT_PROPERTY) + " Value=" +
                                                            doc.get(SENSOR_DATA_DOCUMENT_PROPERTY) + "\n"));
                                } else {
                                    Log.d("ERROR", "get failed with ", task1.getException());
                                }
                            }
                    );
        }

    }

}
