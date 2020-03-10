package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.facebook.LoggingBehavior;
import com.facebook.internal.Logger;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.daher928.bmw_display_system.AppState.getSensorFromId;
import static com.example.daher928.bmw_display_system.AppState.sensorsPreviousConfigNestedMap;

public class PreviousConfigs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_configs);

        View v = getWindow().getDecorView();
        ImageButton back_button = findViewById(R.id.back_button9);
        ImageButton next_button = findViewById(R.id.next_button9);

        if(AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));
            next_button.setImageDrawable(getResources().getDrawable(R.drawable.next_button, null));
        }else{
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));
            next_button.setImageDrawable(getResources().getDrawable(R.drawable.red_next_button, null));
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                startActivity(new Intent(PreviousConfigs.this, ChooseCond.class));
            }
        });

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        TextView textView = findViewById(R.id.textView7);
        textView.setText("Previous Configurations of " + userEmail);

//        sensorsConfigNestedMap = new HashMap<>();

        Log.i(TAG, "config map in PreviousConfigs " + sensorsPreviousConfigNestedMap);

        List<String> displaysList = new ArrayList<>();
        Map<String, Map<String, Object>> displayToInnerConfigMap = new HashMap<>();
        for (String docId : sensorsPreviousConfigNestedMap.keySet()){
            Map<String, Object> innerConfigMap = (Map<String, Object>) sensorsPreviousConfigNestedMap.get(docId);
            String displayStr = "";
            for (String sensor : innerConfigMap.keySet()){
                Map<String, Object> sensorMap = (Map<String, Object>) innerConfigMap.get(sensor);
                long color = (Long) sensorMap.get("color");
                double maxY = (double) sensorMap.get("maxY");
                double resolution = (double) sensorMap.get("resolution");
                SensorConfiguration sensorConfiguration = new SensorConfiguration((int)color, maxY, resolution);
                displayStr += ("ID:" + sensor + " " + sensorConfiguration.toString() + " ");
            }

            displaysList.add(displayStr);
            displayToInnerConfigMap.put(displayStr, innerConfigMap);
            Log.i(TAG, "displayStr " + displayStr);

        }

        ListView listView = findViewById(R.id.companionsearch_listView1);

        String[] list = displaysList.stream().toArray(String[]::new);

        ArrayAdapter adapter = new ArrayAdapter<String>(PreviousConfigs.this, android.R.layout.simple_list_item_single_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        AppState.selectedDiNamesList.clear();
        AppState.selectedIds.clear();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppCompatCheckedTextView checkBox = (AppCompatCheckedTextView) view;

                if (checkBox.isChecked() == true){
                    String selectedTxt = checkBox.getText().toString();
                    Log.i("PreviousConfig", "Selected= " + selectedTxt);
                    Map<String, Object> innerMap = displayToInnerConfigMap.get(selectedTxt);
                    Log.i("PreviousConfig", "innerConfigMap= " + innerMap);

                    for (String sensor : innerMap.keySet()){
                        Map<String, Object> sensorMap = (Map<String, Object>) innerMap.get(sensor);
                        long color = (Long) sensorMap.get("color");
                        double maxY = (double) sensorMap.get("maxY");
                        double resolution = (double) sensorMap.get("resolution");
                        Sensor sensorObj = createSensorWithConfig(sensor, color, maxY, resolution);
                        AppState.selectedIds.add(sensor);
                    }
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                Intent mIntent = new Intent(PreviousConfigs.this, LiveConditions.class);
                mIntent.putExtra("FROM_ACTIVITY", "PreviousConfigs");
                startActivity(mIntent);
            }
        });

    }

    private Sensor createSensorWithConfig(String sensorId, long color, double maxY, double resolution){
        Sensor sensor = getSensorFromId(sensorId);
        sensor.resetConfig();
        sensor.getConfig().setColor((int)color);
        sensor.getConfig().setMaxY(maxY);
        sensor.getConfig().setResolution(resolution);
        return sensor;
    }
}
