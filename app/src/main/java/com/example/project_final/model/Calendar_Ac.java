package com.example.project_final.model;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.project_final.R;

public class Calendar_Ac extends AppCompatActivity {
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_);
        calendarView = findViewById(R.id.calendar_view);
        calendarView.showCurrentMonthPage();
    }
}