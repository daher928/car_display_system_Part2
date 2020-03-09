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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PreviousConfigs extends AppCompatActivity {

    private static Map<String, Object> sensorsConfigNestedMap;

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

        sensorsConfigNestedMap = new HashMap<>();

        ListView listView = findViewById(R.id.companionsearch_listView1);

        String[] list = AppState.getSensorsDiNames();

        ArrayAdapter adapter = new ArrayAdapter<String>(PreviousConfigs.this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        updateConfigsFromFirebase();


    }

    private void updateConfigsFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .collection("sensorsConfigs")
                .get()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                insetToConfigsmap(task1);
                                Log.i(TAG, "config map " + sensorsConfigNestedMap);
                            } else {
                                Log.d("ERROR", "get failed with ", task1.getException());
                            }
                        }
                );
    }

    private void insetToConfigsmap(Task<QuerySnapshot> task1) {
        List<DocumentSnapshot> documents = task1.getResult().getDocuments();
        for (DocumentSnapshot doc : documents){
//            Log.i("doc", doc.getData().toString());
            Map<String, Object> docData = doc.getData();
            for (String sensorId : docData.keySet()){
                Map<String, Object> configs = (Map<String, Object>) docData.get(sensorId);
                configs.put("sensorId" , sensorId);
                sensorsConfigNestedMap.put(doc.getId(), configs);
                Log.i("doc", sensorsConfigNestedMap.toString());
            }
        }
    }
}
