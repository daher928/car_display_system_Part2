package com.example.daher928.bmw_display_system;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

class SocketThread extends Thread implements Runnable {
    static int port = 9191;
    Socket s;
    ServerSocket ss;
    InputStreamReader isr;
    BufferedReader br;
    Handler h = new Handler();
    Context context;
    String message;

    public final String LOG_FILE_NAME = "bmwLog";

    public SocketThread(Context context) {
        this.context = context;
    }

    @Override
    public void run(){

        try{
            ss = new ServerSocket(port);
            while(true) {
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                while((message = br.readLine()) != null){
                    if(message == null)
                        continue;
                    Log.i("SocketThread Received:" , message);

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Timestamp ts = new Timestamp(System.currentTimeMillis());
                            Date d1 = new Date(ts.getTime());
                            String textToSave = d1 + " " + message + "\n";
                            if (AppState.isLogActive){
                                try {
                                    FileOutputStream fileOutputStream = context.openFileOutput("bmwLog", Context.MODE_APPEND);
                                    fileOutputStream.write(textToSave.getBytes());
                                    fileOutputStream.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });

                    AppState.queue.add(message);

                }

            }
        }catch(Exception e){
            Log.i("SocketThread Connection reset:" , "Connection reset");
        }
    }
}