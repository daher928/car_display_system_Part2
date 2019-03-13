package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        View v = getWindow().getDecorView();
        ImageView car_buttons = findViewById(R.id.carButtons);
        if(AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            car_buttons.setImageDrawable(getResources().getDrawable(R.drawable.carbuttons, null));
        }else{
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            car_buttons.setImageDrawable(getResources().getDrawable(R.drawable.red_car_buttons, null));
        }

        Button liveCond_button = findViewById(R.id.liveCond_button);
        Button logStats_button = findViewById(R.id.logStats_button);
        Button options_button = findViewById(R.id.options_button);
        Button about_button = findViewById(R.id.about_button);

        //-- Live Condition page button
        liveCond_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, ChooseCond.class));
                }
            }
        });

        //-- About & Help page button
        about_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, AboutHelp.class));
                }

            }
        });

        //-- Options page button
        options_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, Options.class));
                }

            }
        });

        logStats_button.setOnClickListener(new View.OnClickListener() {
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

        Thread myThread = new Thread(new SocketThread());
        myThread.start();

    }

}
