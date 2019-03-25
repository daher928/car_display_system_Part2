package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class ChooseConfig extends AppCompatActivity {

    String[] arraySpinner = new String[] {
            "Red", "Green", "Black", "Yellow", "Blue"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_configuration);

        View v = getWindow().getDecorView();
        ImageButton back_button = findViewById(R.id.back_button);
        ImageButton next_button = findViewById(R.id.next_button);

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
                startActivity(new Intent(ChooseConfig.this, ChooseCond.class));
            }

        });

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
            startActivity(new Intent(ChooseConfig.this, LiveConditions.class));
            }
        });

        //Table weight - depends on # of selected sensors
        TableLayout table = findViewById(R.id.TableLayoutConfig);
        float weight = ((float) AppState.selectedIds.size()) / (float) 4;
        table.setWeightSum(weight);


        //components
        Sensor sensor1 = AppState.getSensorFromId(AppState.selectedIds.get(0));
        TextView sensor1txtView = findViewById(R.id.sensorName1);
        sensor1txtView.setText(sensor1.getName() + " [0x" + sensor1.getId() + "]");
        Spinner spinner1 =  findViewById(R.id.colorSpinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
//-->Continue from here
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }
}
