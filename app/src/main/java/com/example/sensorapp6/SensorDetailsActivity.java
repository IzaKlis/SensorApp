package com.example.sensorapp6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorLabelTextView;
    private TextView sensorDetailsTextView;
    public static final String KEY_SENSOR_TYPE= "sensor_type";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorLabelTextView = findViewById(R.id.sensor_label);
        sensorDetailsTextView = findViewById(R.id.sensor_details);

        int sensorType = getIntent().getIntExtra(KEY_SENSOR_TYPE, Sensor.TYPE_LIGHT);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        if (sensor == null) {
            sensorLabelTextView.setText(R.string.missing_sensor);
        } else {
            sensorLabelTextView.setText(sensor.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        String sensorInfo = "Type: " + sensorType + "\n Value: " + currentValue;
        switch (sensorType) {
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_LIGHT:
                sensorDetailsTextView.setText(sensorInfo);
                break;
            default:
                sensorDetailsTextView.setText(R.string.missing_info);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}