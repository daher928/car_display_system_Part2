package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseCond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cond);

        final ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button.animate();
                startActivity(new Intent(ChooseCond.this, MainMenu.class));
            }

        });
    }
}
