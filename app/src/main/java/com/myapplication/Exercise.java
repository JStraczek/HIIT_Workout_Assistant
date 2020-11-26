package com.myapplication;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Exercise extends AppCompatActivity{
    private final String name;
    private int rounds;
    private long interval;
    private long rest;
    private long cooldown;
    private long currentTimer;
    private long timeLeft;
    private int currentStage = 0; // current stage ( round = 2 stages: exercise + rest )
    private int currentRound = 0; // current displayed round

    private CountDownTimer countdownTimer;
    private Button startButton;
    private Button skipButton;
    private TextView stageLabel;
    private TextView countdownText;
    private TextView currentRoundText;
    private ProgressBar progressBar;
    private MediaPlayer countdownSound;

    private boolean isRunning;
    private boolean soundPaused;

    public Exercise (String name, long rounds, long interval, long rest, long cooldown){
        this.name = name;
        this.rounds = (int) rounds;
        this.interval = interval * 1000;
        this.rest = rest * 1000;
        this.cooldown = cooldown * 1000;

        this.currentTimer = interval;
        this.timeLeft = interval;
    }

    public String getName() {return name;}

    public void takeControl(){
        // Function that, when called, takes control of views and buttons in workout activity
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

        countdownSound = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        updateTimer();
    }

    public void startTimer(final long time){
        updateTimer();
        countdownTimer = new CountDownTimer(time, 1000){

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
                if (currentStage < rounds * 2) {
                    currentTimer = getNewSetting();
                    startTimer(currentTimer);
                }
                if (currentStage == rounds * 2) {
                    stageLabel.setText(R.string.cooldown);
                    currentTimer = cooldown;
                    startTimer(currentTimer);
                }
                if (currentStage > rounds * 2) {
                    stageLabel.setText(R.string.finished);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    currentStage = 1;
                    skipButton.setEnabled(false);
                }
                this.cancel();
            }
        }.start();
        isRunning = true;
    }

    public void stopTimer(){
        isRunning = false;
        countdownTimer.cancel();
        startButton.setText(R.string.start);
    }

    public void cancel(){
        countdownSound.stop();
        countdownTimer.cancel();
    }

    public void skipStage(){
        countdownTimer.onFinish();
        if (countdownSound.isPlaying()){
            countdownSound.stop();
        }
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

    @SuppressLint("SetTextI18n")
    public void increment_displayed_round(){
        currentRound++;
        currentRoundText.setText("Round: " + currentRound + '/' + rounds);
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
