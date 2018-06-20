package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class AboutHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_help);

        final ImageButton back_button2 = findViewById(R.id.back_button2);
        back_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button2.animate();
                startActivity(new Intent(AboutHelp.this, MainMenu.class));

            }

        });


    }
}
