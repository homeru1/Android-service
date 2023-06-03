package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent intentMyIntentService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentMyIntentService = new Intent(this, MyIntentService.class);
    }


    public void Start_Service(View view) {
        startService(intentMyIntentService);

        }
    public void Stop_Service(View view)  {
        if (intentMyIntentService != null) {
            stopService(intentMyIntentService);
        }

    }

}