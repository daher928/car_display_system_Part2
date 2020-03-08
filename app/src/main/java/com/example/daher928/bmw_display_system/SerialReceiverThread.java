package com.example.daher928.bmw_display_system;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SerialReceiverThread extends Thread implements Runnable {

    //USB SDK Setup required classes
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    Context context;
    Handler h = new Handler();

//    FileOutputStream fileOutputStream;
    private FirebaseFirestore firestore;

    String streamLine = "";

    private static final int SERIAL_BAUD_RATE = 9600;
    private static final int ARDUINO_VENDOR_ID = 0x2341;
    private static final String END_OF_LINE = "\r\n";
    public final String ACTION_USB_PERMISSION = "com.daher.arduinousb.USB_PERMISSION";
    public final String LOG_FILE_NAME = "bmwLog";
    public final String LOG_COLLECTION_NAME = "bmwLog";
    public final String ALL_SENSORS_COLLECTION_NAME = "all_sensors";
    public final String SENSOR_ID_DOCUMENT_PROPERTY = "sensor_id";
    public final String SENSOR_DATA_DOCUMENT_PROPERTY = "sensor_data";
    public final String DATE_DOCUMENT_PROPERTY = "timestamp";
    public final String USER_EMAIL_PROPERTY = "userId";

    public SerialReceiverThread(Context context, UsbManager usbManager) {
        this.context = context;
        this.usbManager = usbManager;
        firestore = FirebaseFirestore.getInstance();
//        File dir = context.getFilesDir();
//        File file = new File(dir, LOG_FILE_NAME);
//        file.delete();
//        try {
//            this.fileOutputStream = context.openFileOutput(LOG_FILE_NAME, Context.MODE_APPEND);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

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

                    StreamLine parsedStream = StreamUtil.parse(singleStream);
                    // Pushing TS#SID#SVAL to queue
                    if (parsedStream != null) {
                        AppState.queue.add(parsedStream.toString());
                    }

                    h.post(() -> {
//                        Timestamp ts = new Timestamp(System.currentTimeMillis());
//                        Date d1 = new Date(ts.getTime());
//                        String textToSave = d1 + " " + singleStream + "\n";
//                            Toast.makeText(context, "singlestream: "+singleStream.toString(), Toast.LENGTH_SHORT).show();
                        if (AppState.isLogActive){
//                              fileOutputStream.write(textToSave.getBytes());
                            // Log to firestore
                            if (AppState.selectedIds.contains(parsedStream.getSensorId())) {
                                Map<String, Object> new_data = new HashMap<>();
                                String sensorId = parsedStream.getSensorId();
                                String sensorData = parsedStream.getSensorData();
                                long timestamp = parsedStream.getTimeStamp();
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                Date date = new Date(timestamp);
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//                            String strDate = dateFormat.format(date);

                                new_data.put(SENSOR_ID_DOCUMENT_PROPERTY, sensorId);
                                new_data.put(SENSOR_DATA_DOCUMENT_PROPERTY, sensorData);
                                new_data.put(DATE_DOCUMENT_PROPERTY, date);
                                new_data.put(USER_EMAIL_PROPERTY, userId);

                                firestore.collection("Users")
                                        .document(userId)
                                        .collection("logs")
                                        .add(new_data)
                                        .addOnCompleteListener(task -> {
                                            if (!task.isSuccessful()) {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        });
                            }
//                            Map<String, Object> sensor_info = new HashMap<>();
//                            sensor_info.put(SENSOR_ID_DOCUMENT_PROPERTY, sensorId);

//                            DocumentReference docRef = firestore
//                                    .collection(ALL_SENSORS_COLLECTION_NAME)
//                                    .document(sensorId);
//                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot document = task.getResult();
//                                        if (!document.exists()) {
//                                            firestore.collection(ALL_SENSORS_COLLECTION_NAME).add(sensor_info);
//                                            Log.d("New sensor", sensorId);
//                                        }
//                                    } else {
//                                        Log.d(TAG, "get failed with ", task.getException());
//                                    }
//                                }
//                            });
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
