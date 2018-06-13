package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        LinearLayout row2 = (LinearLayout) findViewById(R.id.linear_layout);
        Button ivBowl = new Button(this);
        ivBowl.setText("Dynamically Added");
        LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(250, 50);
        layoutParams.setMargins(5, 3, 0, 0); // left, top, right, bottom
        ivBowl.setLayoutParams(layoutParams);
        row2.addView(ivBowl);

        Button ivBowl2 = new Button(this);
        ivBowl2.setText("Dynamically Added 2");
        LinearLayout.LayoutParams layoutParams2 = new  LinearLayout.LayoutParams(250, 50);
        layoutParams.setMargins(5, 3, 0, 0); // left, top, right, bottom
        ivBowl2.setLayoutParams(layoutParams);
        row2.addView(ivBowl2);

        Button ivBowl3 = new Button(this);
        ivBowl3.setText("Dynamically Added 3");
        LinearLayout.LayoutParams layoutParams3 = new  LinearLayout.LayoutParams(250, 50);
        layoutParams.setMargins(5, 3, 0, 0); // left, top, right, bottom
        ivBowl3.setLayoutParams(layoutParams);
        row2.addView(ivBowl3);

    }
}
