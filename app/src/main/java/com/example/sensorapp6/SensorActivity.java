package com.example.sensorapp6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    RecyclerView recyclerView;
    private SensorAdapter adapter;
    private List<Sensor> sensorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public class SensorHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView iconImageView;
        Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            nameTextView = itemView.findViewById(R.id.item_sensor_name);
            iconImageView = itemView.findViewById(R.id.sensor_icon);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            nameTextView.setText(sensor.getName());
            iconImageView.setImageResource(R.drawable.ic_sensor);
        }
    }

    public class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensorList;

        public SensorAdapter(List<Sensor> sensorList) {
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflate = LayoutInflater.from(parent.getContext());
            return new SensorHolder(inflate, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            holder.bind(sensorList.get(position));
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }

}