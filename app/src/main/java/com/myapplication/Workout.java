package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
//import com.myapplication.Exercise;

public class Workout extends AppCompatActivity {
    private TextView stageLabel;
    private Button startButton;
    private Button skipButton;
    private TextView countdownText;
    private TextView currentRoundText;
    private ProgressBar progressBar;
    private CountDownTimer countdownTimer;
//
//    private Exercise exercise;

    private int rounds;

    private long warmup;
    private long interval;
    private long rest;
    private long cooldown;
    private long currentTimer;

    private int currentStage = 0; // current stage ( stages > rounds )
    private int currentRound = 0; // current displayed round

    private boolean isRunning = false;
    private boolean soundPaused = false;

    MediaPlayer countdownSound;

    private CountDownTimer countDownTimer;
    private long timeLeft;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getSupportActionBar().hide();

        countdownSound = MediaPlayer.create(getApplicationContext(), R.raw.countdown);

        getSetup();

        startButton = findViewById(R.id.startButton);
        skipButton = findViewById(R.id.skipButton);
        skipButton.setEnabled(false);

        countdownText = findViewById(R.id.countdownText);
        currentRoundText = findViewById(R.id.currentRoundText);
        currentRoundText.setText("Round: " + currentRound + "/" + rounds);

        stageLabel = findViewById(R.id.stageLabel);
        stageLabel.setText(R.string.warmup);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);

//        exercise = new Exercise("Push-up", 4, 40, 60, 90);
//        exercise.takeControl();
//
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
                skipButton.setEnabled(true);
            }
        }); // will stay in the main activity

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               skipStage();
            }
        });

    }

    @Override
    public void onBackPressed(){
        countdownTimer.cancel();
        super.onBackPressed();
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
                if(countdownText.getText().toString().equals("0:03")){
                    play_countdown_sound(); // play sound for the last 3 seconds
                }
            }

            @Override
            public void onFinish() {
                currentStage++;
                if(currentStage < rounds * 2) {
                    currentTimer = getNewSetting();
                    startTimer(currentTimer);
                }
                if (currentStage == rounds * 2){
                    stageLabel.setText(R.string.cooldown);
                    currentTimer = cooldown;
                    startTimer(currentTimer);
                }
                if (currentStage > rounds * 2){
                    stageLabel.setText(R.string.finished);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    currentStage = 0;
                    skipButton.setEnabled(false);
                }
                this.cancel();
            }
        }.start();
        isRunning = true;
        startButton.setText(R.string.pause);
    }

    public long getNewSetting(){
        if (currentStage % 2 != 0) {
            increment_displayed_round();
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

    public void skipStage(){
        countdownTimer.onFinish();
        if (countdownSound.isPlaying()){
            countdownSound.stop();
        }
    }

    @SuppressLint("SetTextI18n")
    public void increment_displayed_round(){
        currentRound++;
        currentRoundText.setText("Round: " + currentRound + '/' + rounds);
    }

    public void stopTimer(){
        isRunning = false;
        countdownTimer.cancel();
        startButton.setText(R.string.start);
    }

    public void startStop(){
        if(isRunning){
            stopTimer();
            if (countdownSound.isPlaying()){
                countdownSound.pause();
                soundPaused = true;
            }
        }else{
            startTimer(timeLeft);
            if(soundPaused){
                soundPaused = false;
                countdownSound.start();
            }
        }
    }

    public void play_countdown_sound() {
        countdownSound.start();
    }
}

