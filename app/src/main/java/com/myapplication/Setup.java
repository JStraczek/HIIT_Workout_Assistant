package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setup extends AppCompatActivity implements View.OnClickListener {
    private Button setButton;
    private Button resetButton;
    private Bundle timers;

    // Timers
    long rounds;
    long warmupDur;
    long intervalDur;
    long restDur;
    long cooldownDur;

    // Inputs
    EditText roundsInput;
    EditText warmupInput;
    EditText intervalInput;
    EditText restInput;
    EditText cooldownInput;

    // saving preferences
    public static final String PREVIOUS_SETTINGS = "previous_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Initializing inputs
        roundsInput = findViewById(R.id.roundsInput);
        warmupInput = findViewById(R.id.warmupInput);
        intervalInput = findViewById(R.id.intervalInput);
        restInput = findViewById(R.id.restInput);
        cooldownInput = findViewById(R.id.cooldownInput);

        //Initializing buttons (that's a lot of buttons)
        setButton = findViewById(R.id.setButton);
        setButton.setOnClickListener(this);

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);

        Button roundsButton_increase = findViewById(R.id.roundsButton_increase);
        roundsButton_increase.setOnClickListener(this);
        Button roundsButton_decrease = findViewById(R.id.roundsButton_decrease);
        roundsButton_decrease.setOnClickListener(this);
        Button warmupButton_increase = findViewById(R.id.warmupButton_increase);
        warmupButton_increase.setOnClickListener(this);
        Button warmupButton_decrease = findViewById(R.id.warmupButton_decrease);
        warmupButton_decrease.setOnClickListener(this);
        Button intervalButton_increase = findViewById(R.id.intervalButton_increase);
        intervalButton_increase.setOnClickListener(this);
        Button intervalButton_decrease = findViewById(R.id.intervalButton_decrease);
        intervalButton_decrease.setOnClickListener(this);
        Button restButton_increase = findViewById(R.id.restButton_increase);
        restButton_increase.setOnClickListener(this);
        Button restButton_decrease = findViewById(R.id.restButton_decrease);
        restButton_decrease.setOnClickListener(this);
        Button cooldownButton_increase = findViewById(R.id.cooldownButton_increase);
        cooldownButton_increase.setOnClickListener(this);
        Button cooldownButton_decrease = findViewById(R.id.cooldownButton_decrease);
        cooldownButton_decrease.setOnClickListener(this);

        //loading saved prefs (or not if there are none :))
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.setButton:
                try{ // try for checking if there are empty spaces
                    timers = setTimers(); // gets user input and packs it into a bundle
                    if (checkInputs()) { // checking if inputs are valid
                        saveData();
                        startWorkout(timers); // passes bundle of timers to the next activity and starts workout
                    } else {
                        Toast.makeText(getApplicationContext(), "Values can't be 0", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Fill in the blank spaces", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.roundsButton_increase:
                changeEditTextValue(roundsInput, 1, '+');
                break;

            case R.id.roundsButton_decrease:
                changeEditTextValue(roundsInput, 1, '-');
                break;

            case R.id.warmupButton_increase:
                changeEditTextValue(warmupInput, 30, '+');
                break;

            case R.id.warmupButton_decrease:
                changeEditTextValue(warmupInput, 30, '-');
                break;

            case R.id.intervalButton_increase:
                changeEditTextValue(intervalInput, 5, '+');
                break;

            case R.id.intervalButton_decrease:
                changeEditTextValue(intervalInput, 5, '-');
                break;

            case R.id.restButton_increase:
                changeEditTextValue(restInput, 5, '+');
                break;

            case R.id.restButton_decrease:
                changeEditTextValue(restInput, 5, '-');
                break;

            case R.id.cooldownButton_increase:
                changeEditTextValue(cooldownInput, 30, '+');
                break;

            case R.id.cooldownButton_decrease:
                changeEditTextValue(cooldownInput, 30, '-');
                break;

            case R.id.resetButton:
                resetInputs();
                break;

            default:
                break;
        }
    }



    public Bundle setTimers(){
        Bundle bundle = new Bundle();
        rounds =  Long.parseLong(roundsInput.getText().toString());
        warmupDur = Long.parseLong(warmupInput.getText().toString());
        intervalDur = Long.parseLong(intervalInput.getText().toString());
        restDur = Long.parseLong(restInput.getText().toString());
        cooldownDur = Long.parseLong(cooldownInput.getText().toString());

        long[] timers = {rounds, warmupDur, intervalDur, restDur, cooldownDur};
        bundle.putLongArray("timers", timers);

        return bundle;
    }

    public void startWorkout(Bundle bundle){
        Intent intent = new Intent(this, Workout.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    public void changeEditTextValue(EditText editText, long amount, char sign){
        if(sign == '+'){
            editText.setText(Long.toString(Long.parseLong(editText.getText().toString()) + amount));
        }else if (sign == '-'){
            if (!editText.getText().toString().equals("0")) {
                editText.setText(Long.toString(Long.parseLong(editText.getText().toString()) - amount));
            }
        }
    }


    public boolean checkInputs(){
        return rounds > 0 && warmupDur > 0 && intervalDur > 0 && restDur > 0 && cooldownDur >0;
    }

    public void resetInputs(){
        roundsInput.setText("0");
        warmupInput.setText("0");
        intervalInput.setText("0");
        restInput.setText("0");
        cooldownInput.setText("0");
    }

    public void saveData(){
        SharedPreferences previousSettings = getSharedPreferences(PREVIOUS_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = previousSettings.edit();

        editor.putString("Rounds", roundsInput.getText().toString());
        editor.putString( "Warmup", warmupInput.getText().toString());
        editor.putString( "Interval", intervalInput.getText().toString());
        editor.putString( "Rest", restInput.getText().toString());
        editor.putString( "Cooldown", cooldownInput.getText().toString());

        editor.apply();

        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences previousSettings = getSharedPreferences(PREVIOUS_SETTINGS, MODE_PRIVATE);
        roundsInput.setText(previousSettings.getString("Rounds", "0"));
        warmupInput.setText(previousSettings.getString("Warmup", "0"));
        intervalInput.setText(previousSettings.getString("Interval", "0"));
        restInput.setText(previousSettings.getString("Rest", "0"));
        cooldownInput.setText(previousSettings.getString("Cooldown", "0"));

        if (!roundsInput.getText().toString().equals("0")){
            Toast.makeText(this, R.string.settings_loaded, Toast.LENGTH_SHORT).show();
        };
    }
}