package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class Options extends AppCompatActivity {
    View main_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        main_view = getWindow().getDecorView();
        main_view.setBackground(getResources().getDrawable(R.drawable.background_image, null));

        final ImageButton back_button2 = findViewById(R.id.back_button2);
        back_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                back_button2.animate();
                startActivity(new Intent(Options.this, MainMenu.class));

            }

        });

        final ToggleButton theme_button = findViewById(R.id.theme_option_button);

        if(AppTheme.theme == ThemeColor.RED){
            theme_button.setChecked(true);
            main_view.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
            back_button2.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));

        }else{
            main_view.setBackground(getResources().getDrawable(R.drawable.background_image, null));
            back_button2.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));
        }

        theme_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(theme_button.isChecked()){
                    AppTheme.changeTheme(ThemeColor.RED);
                    main_view.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
                    back_button2.setImageDrawable(getResources().getDrawable(R.drawable.red_back_button, null));

                }else{
                    AppTheme.changeTheme(ThemeColor.BLUE);
                    main_view.setBackground(getResources().getDrawable(R.drawable.background_image, null));
                    back_button2.setImageDrawable(getResources().getDrawable(R.drawable.back_button, null));

                }
            }
        });
    }
}
