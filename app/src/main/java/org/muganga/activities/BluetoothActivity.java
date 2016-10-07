package org.muganga.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.muganga.Logs.Logger;
import org.muganga.R;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
            Logger.longToast("Bluetooth is on!");
        }else{
            Logger.longToast("Bluetooth is turned off!");
            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);

            //another check
            if(bluetoothAdapter.isEnabled()){
                Logger.longToast("Bluetooth is now turned on!");
            }
        }

        findPairedDevices();
    }

    private void findPairedDevices() {

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0) {
            for (BluetoothDevice device : pairedDevices) {
                Logger.error(device.getName());

            }
        }
    }
}
