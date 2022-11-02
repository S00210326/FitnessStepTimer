package com.example.stepcountertimer;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 11.0;     // upper mag limit
    private final double LO_STEP = 8.0;      // lower mag limit
    boolean highLimit = false;      // detect high limit
    int counter1 = 0;                // step counter

    CountUpTimer timer;
    TextView counter;

    Button startBtn ;
    Button pauseBtn;
//    Button resumeBtn1;

    //STEPS STUFF
    TextView tvSteps;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBtn = findViewById(R.id.btnStart);
        pauseBtn = findViewById(R.id.btnPause);

//       resumeBtn1 = findViewById(R.id.btnResume);
//       resumeBtn1.setVisibility(View.GONE);

        //TIMER STUFF
        counter = findViewById(R.id.tvCount);
        timer = new CountUpTimer(300000) {  // should be high for the run (ms)
            public void onTick(int second) {
                counter.setText(String.valueOf(second));
            }
        };

        //STEPS STUFF

        tvSteps = findViewById(R.id.tvstepsNum);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void doStart(View view) {
        timer.start();
        Toast.makeText(this, "Started counting", Toast.LENGTH_SHORT).show();


        startBtn.setVisibility((View.GONE));

    }

    public void doStop(View view) {
        timer.cancel();
        Toast.makeText(this, "Stopped Run", Toast.LENGTH_SHORT).show();
//        pauseBtn.setVisibility(View.GONE);
//        resumeBtn1.setVisibility(View.VISIBLE);



    }
    public void doResume(View view){
        timer.start();
//        resumeBtn1.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);

    }

    public void doReset(View view) {
        counter.setText("0");
        Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
        startBtn.setVisibility(View.VISIBLE);
    }

    /*
     * When the app is brought to the foreground - using app on screen
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // get a magnitude number using Pythagorus's Theorem
        double mag = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);


        // you need to do your own mag calculating
        if ((mag > HI_STEP) && (highLimit == false)) {
            highLimit = true;
        }
        if ((mag < LO_STEP) && (highLimit == true)) {
            // we have a step
            counter1++;
            tvSteps.setText(String.valueOf(counter1));
            highLimit = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
//not used
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}