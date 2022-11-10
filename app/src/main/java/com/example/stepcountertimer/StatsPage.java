package com.example.stepcountertimer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StatsPage extends AppCompatActivity {

    TextView metresRan;
    TextView timeRan;
TextView caloriesNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);

        Calendar calendar = Calendar.getInstance();

        String currentDate = DateFormat.getDateInstance().format((calendar.getTime()));
        TextView textviewDate = findViewById(R.id.tvDatetext);
        textviewDate.setText(currentDate);
        metresRan = findViewById(R.id.tvMetresNum);

        timeRan = findViewById(R.id.tvLengthRunNum);

        caloriesNum = findViewById(R.id.tvCaloriesNum);
        //getting information from other intent
        Bundle extras = getIntent().getExtras();
        String steps = extras.getString("stepstaken");
        String time = extras.getString("timetaken");


        //maths for stats page
        Integer step = Integer.parseInt(steps);

        Double metres = step * 0.8;

        Double calories = step * 0.04;


        //setting values for stats
        metresRan.setText(String.valueOf(metres));
        timeRan.setText(time);
        caloriesNum.setText(String.valueOf(calories));





    }
    public void backpage (View view){
        finish();




    }
}