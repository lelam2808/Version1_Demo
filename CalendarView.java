package com.example.project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CalendarView extends AppCompatActivity {

    com.applandeo.materialcalendarview.CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        calendarView = findViewById(R.id.calendar_view);
        calendarView.showCurrentMonthPage();
    }
}