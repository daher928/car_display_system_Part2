package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button liveCond_button = findViewById(R.id.liveCond_button);
        Button logStats_button = findViewById(R.id.logStats_button);
        Button options_button = findViewById(R.id.options_button);
        Button about_button = findViewById(R.id.about_button);

        liveCond_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, ChooseCond.class));
                }

            }
        });

        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, AboutHelp.class));
                }

            }
        });

        logStats_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                //button.setPressed(!button.isPressed());
                //button.setSelected(!button.isSelected());
                if (button.isPressed()) {

                    //TODO startActivity(new Intent(MainMenu.this, ChooseCond.class));
                } else {
                    //Handle de-select state change
                }

            }
        });

        options_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                //Set the button's appearance
                //button.setPressed(!button.isPressed());
                //button.setSelected(!button.isSelected());
                if (button.isPressed()) {

                    //TODO setContentView(R.layout.activity_choose_cond);
                } else {
                    //Handle de-select state change
                }

            }
        });
    }




}
