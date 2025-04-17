package com.example.actividadtres;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

public class MainActivity extends Activity {

    private BluetoothAdapter bluetoothAdapter;
    private TextView statusView;
    private ImageView imageView;
    private EditText fileNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        statusView = findViewById(R.id.statusView);
        imageView = findViewById(R.id.imageView);
        fileNameInput = findViewById(R.id.fileNameInput);

        Button checkBluetooth = findViewById(R.id.checkBluetooth);
        Button toggleBluetooth = findViewById(R.id.toggleBluetooth);
        Button pairedDevices = findViewById(R.id.pairedDevices);
        Button takePhoto = findViewById(R.id.takePhoto);
        Button saveFile = findViewById(R.id.saveFile);

        checkBluetooth.setOnClickListener(v -> {
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                statusView.setText("Bluetooth está activado");
            } else {
                statusView.setText("Bluetooth está desactivado");
            }
        });

        toggleBluetooth.setOnClickListener(v -> {
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                } else {
                    bluetoothAdapter.disable();
                }
            }
        });

        pairedDevices.setOnClickListener(v -> {
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                Set bondedDevices = bluetoothAdapter.getBondedDevices();
                StringBuilder devices = new StringBuilder();
                for (Object device : bondedDevices) {
                    devices.append(device.toString()).append("\n");
                }
                statusView.setText(devices.toString());
            }
        });

        takePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        });

        saveFile.setOnClickListener(v -> {
            String fileName = fileNameInput.getText().toString();
            File path = getExternalFilesDir(null);
            File file = new File(path, fileName + ".txt");
            try {
                FileOutputStream stream = new FileOutputStream(file);
                BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                String content = "Estudiante: Tu Nombre\nBatería: " + level + "%\nVersión Android: " + android.os.Build.VERSION.RELEASE;
                stream.write(content.getBytes());
                stream.close();
                statusView.setText("Archivo guardado");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
