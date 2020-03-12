package com.example.daher928.bmw_display_system.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daher928.bmw_display_system.AppTheme;
import com.example.daher928.bmw_display_system.MainMenu;
import com.example.daher928.bmw_display_system.R;
import com.example.daher928.bmw_display_system.ThemeColor;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationUser extends AppCompatActivity {
    Button btnLogOut;
    Button proceedBtn;
    TextView text;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_user);

        View v = getWindow().getDecorView();
        if(AppTheme.theme == ThemeColor.BLUE) {
            v.setBackground(getResources().getDrawable(R.drawable.background_image, null));
        }else{
            v.setBackground(getResources().getDrawable(R.drawable.red_backround_image, null));
        }

        text = findViewById(R.id.textUser);
        text.setText("Logged in as " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(AuthenticationUser.this, AuthenticationLogin.class);
                startActivity(I);

            }
        });

        proceedBtn = (Button) findViewById(R.id.proceedBtn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent I = new Intent(AuthenticationUser.this, MainMenu.class);
                startActivity(I);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nth;
    }

}