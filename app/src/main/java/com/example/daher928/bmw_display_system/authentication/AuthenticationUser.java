package com.example.daher928.bmw_display_system.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daher928.bmw_display_system.AppTheme;
import com.example.daher928.bmw_display_system.R;
import com.example.daher928.bmw_display_system.ThemeColor;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationUser extends AppCompatActivity {
    Button btnLogOut;
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

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(AuthenticationUser.this, AuthenticationLogin.class);
                startActivity(I);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nth;
    }

}