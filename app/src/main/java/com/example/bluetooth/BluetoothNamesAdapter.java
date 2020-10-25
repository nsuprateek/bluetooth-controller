package com.example.bluetooth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BluetoothNamesAdapter extends RecyclerView.Adapter<BluetoothNamesAdapter.ViewHolder> {

    private String deviceNames[];
    private String deviceAddresses[];
    //    private Context context;

    public BluetoothNamesAdapter(ArrayList<String> deviceNames, ArrayList<String> deviceAddresses) {
        this.deviceNames = deviceNames.toArray(new String[deviceNames.size()]);
        this.deviceAddresses = deviceAddresses.toArray(new String[deviceAddresses.size()]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView addressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            addressTextView = (TextView) itemView.findViewById(R.id.addressTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View nameView = inflater.inflate(R.layout.bluetooth_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(nameView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameTextView.setText(deviceNames[position]);
        holder.addressTextView.setText(deviceAddresses[position]);

    }

    @Override
    public int getItemCount() {
        return deviceNames.length;
    }

}
