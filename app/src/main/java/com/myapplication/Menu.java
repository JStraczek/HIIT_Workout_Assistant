package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    private Button startButton;
    private Button darkmodeButton;

    private boolean darkmodeON = false;

    public static final String DARKMODE_STATUS = "darkmodeStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSetup();
            }
        });

        darkmodeButton = findViewById(R.id.darkmodeButton);
        darkmodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences darkmodeStatus = getSharedPreferences(DARKMODE_STATUS, MODE_PRIVATE);
                SharedPreferences.Editor editor = darkmodeStatus.edit();
                if (!darkmodeON){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkmodeON = true;
                    editor.putBoolean("darkmode_status", darkmodeON);

                    Toast.makeText(getApplicationContext(), "Dark mode enabled", Toast.LENGTH_SHORT).show();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkmodeON = false;
                    editor.putBoolean("darkmode_status", darkmodeON);

                    Toast.makeText(getApplicationContext(), "Dark mode disabled", Toast.LENGTH_SHORT).show();
                }
                editor.apply();


            }
        });

        SharedPreferences darkmodeStatus = getSharedPreferences(DARKMODE_STATUS, MODE_PRIVATE);
        darkmodeON = darkmodeStatus.getBoolean("darkmode_status", false);
        if(darkmodeON){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkmodeButton.setText(R.string.dm_status);
        }

    }


    public void openSetup(){
        Intent intent = new Intent(this, Setup.class);

        startActivity(intent);
    }
}