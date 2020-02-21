package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ChooseGrid extends AppCompatActivity {
    ToggleButton list_button ;
    ToggleButton blocks_button ;
    ToggleButton slides_button ;

    private final static String PLEASE_SELECT_GRID_MESSAGE = "Please Select Grid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_grid);
        AppState.gridSelected = null;
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

                startActivity(new Intent(ChooseGrid.this, ChooseCond.class));
            }

        });

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if(AppState.gridSelected != null)
                    startActivity(new Intent(ChooseGrid.this, LiveConditions.class));
                else
                    Toast.makeText(getApplicationContext(),PLEASE_SELECT_GRID_MESSAGE , Toast.LENGTH_SHORT).show();
            }
        });

        list_button = findViewById(R.id.list_button);
        blocks_button = findViewById(R.id.blocks_button);
        slides_button = findViewById(R.id.slides_button);

        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_button.isChecked()){
                    AppState.gridSelected = AppState.LIST_GRID;
                    if(blocks_button.isChecked())
                        blocks_button.setChecked(false);
                    if(slides_button.isChecked())
                        slides_button.setChecked(false);
                }else{
                    AppState.gridSelected = null;
                }
            }
        });

        blocks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blocks_button.isChecked()){
                    AppState.gridSelected = AppState.BLOCKS_GRID;
                    if(list_button.isChecked())
                        list_button.setChecked(false);
                    if(slides_button.isChecked())
                        slides_button.setChecked(false);
                }else{
                    AppState.gridSelected = null;
                }
            }
        });

        slides_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slides_button.isChecked()){
                    AppState.gridSelected = AppState.SLIDES_GRID;
                    if(list_button.isChecked())
                        list_button.setChecked(false);
                    if(blocks_button.isChecked())
                        blocks_button.setChecked(false);
                }else{
                    AppState.gridSelected = null;
                }
            }
        });

    }
}
