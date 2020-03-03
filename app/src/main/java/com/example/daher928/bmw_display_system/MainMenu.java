package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.daher928.bmw_display_system.authentication.AuthenticationLogin;
import com.example.daher928.bmw_display_system.authentication.AuthenticationUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    static Thread myThread = null;

    private FirebaseAuth firebaseAuth;
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
        Button btnLogOut = (Button) findViewById(R.id.btnLogOut);

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
                if (button.isPressed()) {
                    startActivity(new Intent(MainMenu.this, LogStats.class));
                }

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(MainMenu.this, AuthenticationLogin.class);
                startActivity(I);

            }
        });

        if(myThread==null){
            myThread = new Thread(new SerialReceiverThread(getApplicationContext(),
                    (UsbManager) getSystemService(this.USB_SERVICE)));
            myThread.start();
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please Sign in", Toast.LENGTH_LONG).show();
            Intent I = new Intent(MainMenu.this, AuthenticationLogin.class);
            startActivity(I);
            finish();
        } else {
            Toast.makeText(this, "User: " + user.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Do nth;
    }


}
