package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Workout extends AppCompatActivity {
    private TextView stageLabel;
    private Button startButton;
    private TextView countdownText;
    private ProgressBar progressBar;
    private CountDownTimer countdownTimer;

    private int rounds;

    private long warmup;
    private long interval;
    private long rest;
    private long cooldown;
    private long currentTimer;

    private int currentRound = 0;
    private boolean isRunning = false;

    MediaPlayer countdownSound;

    //private CountDownTimer countDownTimer;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getSupportActionBar().hide();

        countdownSound = MediaPlayer.create(getApplicationContext(), R.raw.countdown);

        getSetup();

        startButton = findViewById(R.id.startButton);
        countdownText = findViewById(R.id.countdownText);

        stageLabel = findViewById(R.id.stageLabel);
        stageLabel.setText(R.string.warmup);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });
        updateTimer();
    }

        public void getSetup(){
            Intent in = getIntent();
            Bundle b = in.getExtras();
            assert b != null;
            long[] timers = b.getLongArray("timers");

            assert timers != null;
            rounds = (int) timers[0];
            warmup = timers[1] * 1000;
            interval = timers[2] * 1000;
            rest = timers[3] * 1000;
            cooldown = timers[4] * 1000;

            currentTimer = warmup;
            timeLeft = warmup;
        }

        public void startTimer(final long time){
            updateTimer();
            countdownTimer = new CountDownTimer(time, 1000) {

                @Override
                public void onTick(long l) {
                    timeLeft = l;
                    updateTimer();
                    if((int) timeLeft/1000 == 3){
                        play_countdown_sound(); // play sound for the last 3 seconds
                    }
                }

                @Override
                public void onFinish() {
                    currentRound++;
                    if(currentRound < rounds * 2) {
                        currentTimer = getNewSetting();
                        startTimer(currentTimer);
                    }
                    if (currentRound == rounds * 2){
                        stageLabel.setText(R.string.cooldown);
                        currentTimer = cooldown;
                        startTimer(currentTimer);
                    }
                    if (currentRound > rounds * 2){
                        stageLabel.setText(R.string.finished);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        currentRound = 0;
                    }
                }
            }.start();
            isRunning = true;
            startButton.setText(R.string.pause);
        }

        public long getNewSetting(){
            if (currentRound % 2 != 0) {
                stageLabel.setText(R.string.exercise);
                return interval;
            } else {
                stageLabel.setText(R.string.rest);
                return rest;
            }
        }

        public void updateTimer(){
            if (currentTimer != 0) {
                progressBar.setProgress((int) ((int) 100 * (currentTimer - timeLeft) / currentTimer));
            }
            int minutes = (int) timeLeft / 60000;
            int seconds = (int) timeLeft % 60000 / 1000;

            String timeLeftText;

            timeLeftText = "" + minutes;
            timeLeftText += ":";
            if(seconds < 10) timeLeftText += "0";
            timeLeftText += seconds;
            countdownText.setText(timeLeftText);
        }

        public void stopTimer(){
            isRunning = false;
            countdownTimer.cancel();
            startButton.setText(R.string.start);
        }

        public void startStop(){
            if(isRunning){
                stopTimer();
            }else{
                startTimer(timeLeft);
            }
        }

        public void play_countdown_sound(){
            countdownSound.start();
        }
    }

