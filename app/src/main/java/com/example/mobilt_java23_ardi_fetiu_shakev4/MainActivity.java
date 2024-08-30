package com.example.mobilt_java23_ardi_fetiu_shakev4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textView;
    private ImageView imageView;
    private SeekBar seekBar;
    private Button button;

    private float[] accelerometerValues = new float[3];
    private static final float SHAKE_THRESHOLD = 12.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hitta UI-komponenterna
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);

        // Konfigurera sensorer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Reset-knappens funktion
        button.setOnClickListener(v -> resetSensorData());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerValues, 0, event.values.length);
            float x = accelerometerValues[0];
            float y = accelerometerValues[1];
            float z = accelerometerValues[2];

            // Visa accelerometerdata
            textView.setText(String.format("X: %.2f, Y: %.2f, Z: %.2f", x, y, z));

            // Kontrollera om en skakning inträffar
            float accelerationMagnitude = (float) Math.sqrt(x * x + y * y + z * z);
            if (accelerationMagnitude > SHAKE_THRESHOLD) {
                Toast.makeText(this, "Shake Detected!", Toast.LENGTH_SHORT).show();
            }

            // Använd accelerometerdatan för att rotera bilden
            imageView.setRotation(x * 10);
            seekBar.setProgress((int) Math.abs(x * 10));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void resetSensorData() {
        textView.setText("Accelerometer Data");
        imageView.setRotation(0);
        seekBar.setProgress(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
