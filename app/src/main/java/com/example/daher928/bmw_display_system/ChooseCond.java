package com.example.daher928.bmw_display_system;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseCond extends AppCompatActivity {

    final static int MAX_ALLOWED_SELECTIONS = 3;
    static List<Integer> selectedPositionsList = new ArrayList<>();

    private final static String PLEASE_SELECT_CONDITION_MESSAGE = "Please Select Conditions";

    static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cond);

        //read sensors.csv file into list
        CSVReader.readSensorsCSV(getResources().openRawResource(R.raw.sensors));

        View v = getWindow().getDecorView();
        final ImageButton back_button = findViewById(R.id.back_button);
        final ImageButton next_button = findViewById(R.id.next_button);
        final Button clearAllButton = findViewById(R.id.clearAllButton);

        if (AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));
            next_button.setImageDrawable(getResources().getDrawable(R.drawable.next_button, null));
        } else {
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            back_button.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));
            next_button.setImageDrawable(getResources().getDrawable(R.drawable.red_next_button, null));
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button.animate();
                startActivity(new Intent(ChooseCond.this, MainMenu.class));
            }

        });

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                next_button.animate();
                if(selectedPositionsList.size()>0)
                    startActivity(new Intent(ChooseCond.this, ChooseConfig.class));
                else
                    Toast.makeText(getApplicationContext(),PLEASE_SELECT_CONDITION_MESSAGE, Toast.LENGTH_SHORT).show();
            }

        });

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Integer position : selectedPositionsList){
                    listView.setItemChecked(position,false);
                }
                AppState.selectedDiNamesList.clear();
                selectedPositionsList.clear();
                AppState.selectedIds.clear();
            }
          });

        listView = findViewById(R.id.companionsearch_listView1);
        String[] list = AppState.getSensorsDiNames();
        ArrayAdapter adapter = new ArrayAdapter<String>(ChooseCond.this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        for(Integer position : selectedPositionsList){
            listView.setItemChecked(position,true);
        }
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppCompatCheckedTextView checkBox = (AppCompatCheckedTextView) view;

                Log.i("Choose Condition", "SelectedList= ");
                if (checkBox.isChecked() == true){
                    if(AppState.selectedDiNamesList.size()==MAX_ALLOWED_SELECTIONS){ //Limit selections to 4
                        listView.setItemChecked(i,false);
                        return;
                    }
                    AppState.selectedDiNamesList.add(checkBox.getText().toString());
                    selectedPositionsList.add(i);
                    AppState.selectedIds.add(AppState.getSensorIdFromDiName(checkBox.getText().toString()));
                }else{
                    AppState.selectedDiNamesList.remove(checkBox.getText().toString());
                    selectedPositionsList.remove((Object)i);
                    AppState.selectedIds.remove(AppState.getSensorIdFromDiName(checkBox.getText().toString()));
                }
                for(String selected : AppState.selectedDiNamesList) {
                    Log.i("-", selected);
                }
                for(String id : AppState.selectedIds) {
                    Log.i("-", id);
                }
            }
        });

        Button prevConfigs_button = (Button) findViewById(R.id.prevButton);

        prevConfigs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseCond.this, PreviousConfigs.class));
            }
        });
    }

}
