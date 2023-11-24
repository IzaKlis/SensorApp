package com.example.sensorapp6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    RecyclerView recyclerView;
    private SensorAdapter adapter;
    private List<Sensor> sensorList;
    private boolean subtitleVisible = false;


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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateSubtitle() {
        int sensorsCount = sensorList.size();

        String subtitle = getString(R.string.sensors_count, sensorsCount);
        if (!subtitleVisible) {
            subtitle = null;
        }
        getSupportActionBar().setSubtitle(subtitle);
    }

    public class SensorHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView iconImageView;
        Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            nameTextView = itemView.findViewById(R.id.item_sensor_name);
            iconImageView = itemView.findViewById(R.id.sensor_icon);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showSensorDetailsDialog();
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v);
                    Sensor selectedSensor = sensorList.get(position);
                    Intent intent = null;
                    if(selectedSensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                        intent= new Intent(SensorActivity.this,LocationActivity.class);
                    }else{
                        intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    }
                    intent.putExtra("sensor_type", selectedSensor.getType());
                    startActivity(intent);
                }
            });
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            int sensorType=sensor.getType();
            nameTextView.setText(sensor.getName());
            iconImageView.setImageResource(R.drawable.ic_sensor);
            if (sensorType == Sensor.TYPE_LIGHT|| sensorType==Sensor.TYPE_PRESSURE || sensorType==Sensor.TYPE_MAGNETIC_FIELD) {
                itemView.setBackgroundColor(Color.rgb(211, 211, 211));
            }
        }

        public void showSensorDetailsDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(sensor.getName());
            builder.setMessage("Producent: " + sensor.getVendor() + "\n"
                    + "Maksymana zwracana wartość: " + sensor.getMaximumRange());
            builder.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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