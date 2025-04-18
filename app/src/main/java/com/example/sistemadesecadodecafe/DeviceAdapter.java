package com.example.sistemadesecadodecafe;

import android.bluetooth.BluetoothDevice;

import java.util.List;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DeviceAdapter  extends ArrayAdapter<BluetoothDevice>{

    private final LayoutInflater inflater;

    public DeviceAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, 0, devices);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.list_item_layout, parent, false);
        }

        BluetoothDevice device = getItem(position);

        ImageView imageViewDevice = itemView.findViewById(R.id.imageViewDevice);
        TextView textViewDeviceInfo = itemView.findViewById(R.id.textViewDeviceInfo);
        textViewDeviceInfo.setText(device.getName() + "\n" + device.getAddress());
        return itemView;
    }
}
