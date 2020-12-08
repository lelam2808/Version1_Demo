package com.example.project_final.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.project_final.R;

public class LoadSceen extends AppCompatActivity {
    private static  int SPLAT_TIME_OUT=2000;
    private int x=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_sceen);
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent time_out=new Intent(getApplicationContext(),login.class);
                    startActivity(time_out);
                    finish();
                }
            },SPLAT_TIME_OUT);
        }catch (Exception e)
        {
            Log.d("time_out",e.getMessage());
        }
    }
}