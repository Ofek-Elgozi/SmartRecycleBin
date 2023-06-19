package com.example.srb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    NavController navCtrl;
    static BluetoothSocket btSocket = null;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;
    private MainActivity context;
    private InputStream inputStream;
    private Thread workerThread;
    private boolean stopWorker;

    private static final String CHARACTER_ENCODING = "UTF-8"; // Modify this if needed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_CONNECT};
        int requestCode = 1; // Choose any value you prefer
        ActivityCompat.requestPermissions(this, permissions, requestCode);
        //Bluetooth stuff
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:18:91:D8:3B:DC"); // Specific MAC address to my Bt device
        //Making the connection
        int counter = 0;
        do {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSION);
                return;
            }

            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);

                context = this;
                Thread connectThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            btSocket.connect();
                            System.out.println(btSocket.isConnected());

                            // Start reading data from the Bluetooth connection
                            startReadingData();

                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle connection failure here
                            // For example, you can break the loop and show an error message to the user
                        }
                    }
                });
                connectThread.start();
                connectThread.join(); // Wait for the connection to complete

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Handle connection failure here
                // For example, you can break the loop and show an error message to the user
                break;
            }
            counter++;
        } while (!btSocket.isConnected() && counter < 3);

        // Sending a test signal
        if (btSocket.isConnected()) {
            try {
                OutputStream outputStream = btSocket.getOutputStream();
                outputStream.write('A');
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle connection failure here
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment nav_host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtrl = nav_host.getNavController();
    }

    @Override
    public void onBackPressed() {
        // Your code to handle the back button press
        // ...
    }

    private void startReadingData() {
        try {
            inputStream = btSocket.getInputStream();

            stopWorker = false;
            workerThread = new Thread(new Runnable() {
                public void run() {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    StringBuilder stringBuilder = new StringBuilder();

                    try {
                        while ((bytesRead = inputStream.read(buffer)) > 0) {
                            String data = new String(buffer, 0, bytesRead);
                            stringBuilder.append(data);

                            // Check if the received data contains a complete number
                            if (data.contains("\n")) {
                                // Extract the number from the received data
                                String numberString = stringBuilder.toString().trim();

                                // Parse the number as an integer
                                try {
                                    int number = Integer.parseInt(numberString);
                                    // Use the number in your application as needed
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            // Update UI or use the number

                                            if(number==0){
                                                Toast.makeText(MainActivity.this, "HIGH TEMPERATURE! RUN!!!", Toast.LENGTH_LONG).show();
                                            } else if (number==1) {
                                                Toast.makeText(MainActivity.this, "REMOVE YOYR HAND BITCH!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                        }
                                    });
                                }

                                // Clear the StringBuilder for the next number
                                stringBuilder.setLength(0);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });
                    }
                }
            });

            workerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stopReadingData() {
        stopWorker = true;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopReadingData();
        try {
            if (btSocket != null) {
                btSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
