package com.example.daher928.bmw_display_system;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class SocketThread extends Thread
        implements Runnable {
    static int port = 9191;
    Socket s;
    ServerSocket ss;
    InputStreamReader isr;
    BufferedReader br;
    Handler h = new Handler();

    String message;

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
                    AppState.receivedData.add(message);
                    AppState.queue.add(message);

                }


            }

        }catch(Exception e){
            Log.i("SocketThread Connection reset:" , "Connection reset");
            e.printStackTrace();
        }
    }
}