package com.example.daher928.bmw_display_system;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogStats extends AppCompatActivity {

    static TextView completeLogView;

    public final String LOG_FILE_NAME = "bmwLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_stats);

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
                startActivity(new Intent(LogStats.this, MainMenu.class));

            }

        });

        completeLogView = findViewById(R.id.completeLogTextView);
        completeLogView.setMovementMethod(new ScrollingMovementMethod());

        Button refreshButton = findViewById(R.id.refreshButtonShadow);
        final Switch filterSwitch = findViewById(R.id.filterSwitch);

        readLog(false);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readLog(filterSwitch.isChecked());
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    readLog(true);
                }else{
                    readLog(false);
                }
            }
        });

    }

    void readLog(boolean isFiltered){
        if (isFiltered && AppState.selectedIds.size()==0)
            return;
        try {
            FileInputStream fileInputStream = openFileInput(LOG_FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                String[] tokens2 = lines.split(" ");
                String stream = tokens2[tokens2.length-1];
                StreamLine streamLine = StreamUtil.parse(stream);
                if (isFiltered){
                    if(AppState.selectedIds.contains(streamLine.sensorId))
                        completeLogView.append(lines + "\n");
                } else {
                    completeLogView.append(lines + "\n");
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
