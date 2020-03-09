package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

            Log.i(TAG, "displayStr " + displayStr);

        }


        ListView listView = findViewById(R.id.companionsearch_listView1);

        String[] list = displaysList.stream().toArray(String[]::new);

        ArrayAdapter adapter = new ArrayAdapter<String>(PreviousConfigs.this, android.R.layout.simple_list_item_single_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);




    }
}
