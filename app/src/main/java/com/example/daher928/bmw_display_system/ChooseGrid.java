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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_grid);

        final ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button.animate();
                startActivity(new Intent(ChooseGrid.this, ChooseCond.class));
            }

        });

        //TODO: NEXT BUTTON
//        final ImageButton next_button = findViewById(R.id.next_button);
//        next_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View button) {
//                next_button.animate();
//                startActivity(new Intent(ChooseGrid.this, ChooseGrid.class));
//            }
//        });

        list_button = findViewById(R.id.list_button);
        blocks_button = findViewById(R.id.blocks_button);
        slides_button = findViewById(R.id.slides_button);

        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_button.isChecked()){
                    if(blocks_button.isChecked())
                        blocks_button.setChecked(false);
                    if(slides_button.isChecked())
                        slides_button.setChecked(false);
                }
            }
        });

        blocks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blocks_button.isChecked()){
                    if(list_button.isChecked())
                        list_button.setChecked(false);
                    if(slides_button.isChecked())
                        slides_button.setChecked(false);
                }
            }
        });

        slides_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slides_button.isChecked()){
                    if(list_button.isChecked())
                        list_button.setChecked(false);
                    if(blocks_button.isChecked())
                        blocks_button.setChecked(false);
                }
            }
        });

    }
}
