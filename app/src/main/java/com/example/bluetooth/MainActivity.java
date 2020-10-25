package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private BluetoothNamesAdapter adapter;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> deviceNames = new ArrayList<String>();
        ArrayList<String> deviceAddresses = new ArrayList<String>();

        deviceNames.ensureCapacity(10);
        deviceAddresses.ensureCapacity(10);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {

            Log.i(TAG, "Bluetooth devices found ");

            for (BluetoothDevice device : pairedDevices) {

                deviceNames.add(0, device.getName());
                deviceAddresses.add(0, device.getAddress());

                Log.i(TAG, device.getName());
                Log.i(TAG, device.getAddress());
            }
        }


        RecyclerView namesView = (RecyclerView) findViewById(R.id.namesList);
        adapter = new BluetoothNamesAdapter(deviceNames, deviceAddresses);
        namesView.setAdapter(adapter);
        namesView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        namesView.addItemDecoration(itemDecoration);

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult: bt on");
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            }
            else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.i(TAG, device.getName());

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                Log.i(TAG, "onReceive: bt found");
                Toast.makeText(getApplicationContext(), "BT found", Toast.LENGTH_SHORT).show();
                bluetoothAdapter.cancelDiscovery();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.i(TAG, "onReceive: bt connected");
                Toast.makeText(getApplicationContext(), "BT Connected", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.i(TAG, "onReceive: bt disconnected");
                Toast.makeText(getApplicationContext(), "BT Disconnected", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i(TAG, "onReceive: discovery started");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG, "onReceive: discovery finished");
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Toast.makeText(getApplicationContext(), "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

}
