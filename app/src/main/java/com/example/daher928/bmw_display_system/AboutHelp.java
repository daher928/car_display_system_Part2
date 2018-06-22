package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

public class AboutHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_help);

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
                startActivity(new Intent(AboutHelp.this, MainMenu.class));

            }

        });

    }
}
