package com.ihandilnath.gameofflags;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GuessCountryActivity extends AppCompatActivity {

    private Spinner mCountrySpinner;
    private CountryRepository countryRepository;
    private Country country;

    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private Button mSubmitButton;
    private TextView mTvResult;
    private TextView mTvAnswer;

    private int mTimeElapsed;
    private boolean mIsTimed;
    private Timer timer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_country);

        // Creating required references to UI elements
        mTimerProgressBar = findViewById(R.id.guesscountry_pb_timer);
        mTimerText = findViewById(R.id.guesscountry_tv_timer);
        mSubmitButton = findViewById(R.id.guesscountry_button_submit);
        mTvResult = findViewById(R.id.guesscountry_tv_result);
        mTvAnswer = findViewById(R.id.guesscountry_tv_correct_answer);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        try {
            countryRepository = CountryRepository.getInstance(this);
        } catch (IOException|JSONException e) {
            Toast.makeText(this,"Error while loading game data. Unable to start game.",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        country = countryRepository.getRandomCountry();
        mIsTimed = getIntent().getExtras().getBoolean("timer");

        populateCountries();

        initGame();

    }

    // UI methods
    private void setFlag() {
        ImageView mFlagImageView = findViewById(R.id.guesscountry_image_flag);
        Drawable flag = countryRepository.getFlagForCountry(country);
        mFlagImageView.setImageDrawable(flag);
        mFlagImageView.setVisibility(View.VISIBLE);
    }

    private void populateCountries() {
        mCountrySpinner = findViewById(R.id.guesscountry_spinner_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, countryRepository.getAllCountryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        resetTimer();
        super.onDestroy();
    }

    private void showTimerIfTimed(){
        if(mIsTimed){
            mTimerProgressBar.setVisibility(View.VISIBLE);
            mTimerText.setVisibility(View.VISIBLE);
            runTimer();
        }
    }

    private void submit(){

        resetTimer();

        String userAnswer = ((String)mCountrySpinner.getSelectedItem());
        if(mCountrySpinner.getSelectedItem().equals(country.getName())){
            mTvResult.setText("Your answer is correct");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            mTvResult.setText("Your answer is wrong");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mTvAnswer.setText("Correct answer is "+country.getName());
        }

        toggleSubmitButton();

    }

    // Business Logic methods
    private void initGame(){
        setFlag();
        showTimerIfTimed();
    }

    public void next(){
        toggleSubmitButton();
        clearResult();
        country = countryRepository.getRandomCountry();
        initGame();
    }

    private void runTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTimeElapsed++;
                        mTimerText.setText(String.format("TIME LEFT: %ds",10 - mTimeElapsed));
                        mTimerProgressBar.setProgress((int)((mTimeElapsed/10.0)*100.0));
                        if(10 - mTimeElapsed == 0){
                            submit();
                        }
                    }
                });
            }
        },0,1000);
    }

    private void resetTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
            mTimeElapsed = 0;
        }
    }

    private void clearResult(){
        mTvResult.setText("");
        mTvAnswer.setText("");
    }

    private void toggleSubmitButton(){
        switch (mSubmitButton.getText().toString()){
            case "Submit":
                mSubmitButton.setText("Next");
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        next();
                    }
                });
                break;
            case "Next":
                mSubmitButton.setText("Submit");
                mSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submit();
                    }
                });
                break;

        }
    }

}
