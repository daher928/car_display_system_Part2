package com.example.daher928.bmw_display_system;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SerialReceiverThread extends Thread implements Runnable {

    //USB SDK Setup required classes
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    Context context;
    Handler h = new Handler();

    FileOutputStream fileOutputStream;

    String streamLine = "";

    private static final int SERIAL_BAUD_RATE = 9600;
    private static final int ARDUINO_VENDOR_ID = 0x2341;
    private static final String END_OF_LINE = "\r\n";
    public final String ACTION_USB_PERMISSION = "com.daher.arduinousb.USB_PERMISSION";

    public SerialReceiverThread(Context context, UsbManager usbManager) {
        this.context = context;
        this.usbManager = usbManager;
        File dir = context.getFilesDir();
        File file = new File(dir, "bmwLog");
        file.delete();
        try {
            this.fileOutputStream = context.openFileOutput("bmwLog", Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        //Receiver to detect any usb connected device
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(broadcastReceiver, filter);

        //Check connected devices and connect to the device.
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == ARDUINO_VENDOR_ID)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent
                            .getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);

                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {

                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);

                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);

                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(SERIAL_BAUD_RATE);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);

                            Toast.makeText(context,"Serial Connection Opened!", Toast.LENGTH_SHORT);

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(context, "USB Device attached", Toast.LENGTH_SHORT);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(context, "USB Device detached", Toast.LENGTH_SHORT);
            }
        }
    };

    //--------------- USB SERIAL HANDLING LOGIC -----------------//
    //Callback for any data received from the USB device

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {

            //Here we receive the data
            try {
                String data = new String(arg0);

                streamLine += data;

                if (streamLine.contains(END_OF_LINE)){
                    String[] split = streamLine.split(END_OF_LINE);
                    final String singleStream = split[0];
                    streamLine = split.length>0 ? split[1] : "";
//                    Log.d("singleStream =", singleStream);

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Timestamp ts = new Timestamp(System.currentTimeMillis());
                            Date d1 = new Date(ts.getTime());
                            String textToSave = d1 + " " + singleStream + "\n";
//                            Toast.makeText(context, "singlestream: "+singleStream.toString(), Toast.LENGTH_SHORT).show();

                            if (AppState.isLogActive){
                                try {
                                    fileOutputStream.write(textToSave.getBytes());

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    StreamLine parsedStream = StreamParser.parse(singleStream);
                    // Pushing TS#SID#SVAL to queue
                    if (parsedStream != null) {
                        AppState.queue.add(parsedStream.toString());
//                                Toast.makeText(context, "pushed to queue "+parsedStream.toString(), Toast.LENGTH_SHORT).show();

                    }
//                            else {
//                                Toast.makeText(context, "parsedStream is null!!!", Toast.LENGTH_SHORT).show();
////                                Log.d(" *** SerialRecieverThread: ","parsedStream is null!!!" );
//                            }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
