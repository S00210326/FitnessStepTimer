package com.example.stepcountertimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener  {
    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 11.0;     // upper mag limit
    private final double LO_STEP = 8.0;      // lower mag limit
    boolean highLimit = false;      // detect high limit
    int counter1 = 0;                // step counter

    CountUpTimer timer;
    TextView timecounter;


    Button startBtn ;
    Button pauseBtn;
    Button statBtn;

    //steps stuff
    TextView tvStepsNum;

    private SensorManager mSensorManager;
    private Sensor mSensor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBtn = findViewById(R.id.btnStart);
        pauseBtn = findViewById(R.id.btnPause);

        statBtn = findViewById(R.id.btnStats);



        //TIMER STUFF
        timecounter = findViewById(R.id.tvCount);
        timer = new CountUpTimer(1000000) {  // should be high for the run (ms)
            public void onTick(int second) {
                timecounter.setText(String.valueOf(second));
            }
        };

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //COUNTER STUFF
        tvStepsNum = findViewById(R.id.tvstepsNum);
        // we are going to use the sensor service

    }

//BUTTON FOR START
    public void doStart(View view) {
        timer.start();
        Toast.makeText(this, "Started counting", Toast.LENGTH_SHORT).show();


        startBtn.setVisibility((View.GONE));

        statBtn.setVisibility((View.GONE));

        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
//BUTTON FOR STOP
    public void doStop(View view) {
        timer.cancel();
        Toast.makeText(this, "Stopped Run", Toast.LENGTH_SHORT).show();

        statBtn.setVisibility((View.VISIBLE));

        mSensorManager.unregisterListener(this);


    }


    //RESET BUTTON
    public void doReset(View view) {
        timer.cancel();
        timecounter.setText("0");

        Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
        startBtn.setVisibility(View.VISIBLE);
    }



    protected void onResume() {
        super.onResume();

    }
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];



        // get a magnitude number using Pythagorus's Theorem
        double mag = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);


        // for me! if msg > 11 and then drops below 9, we have a step
        // you need to do your own mag calculating
        if ((mag > HI_STEP) && (highLimit == false)) {
            highLimit = true;
        }
        if ((mag < LO_STEP) && (highLimit == true)) {
            // we have a step
            counter1++;
            tvStepsNum.setText(String.valueOf(counter1));
            highLimit = false;
        }
    }
    public void ShowStats(View view){

        if (counter1 >= 1 ){
            Intent intent = new Intent(this, StatsPage.class);
            startActivity(intent);
            String steps = tvStepsNum.getText().toString();
            String time = timecounter.getText().toString();

            intent.putExtra("stepstaken",steps);
            intent.putExtra("timetaken", time);

            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Need to Run first", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}