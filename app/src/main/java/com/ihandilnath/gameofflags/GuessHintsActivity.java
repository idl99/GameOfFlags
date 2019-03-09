package com.ihandilnath.gameofflags;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GuessHintsActivity extends AppCompatActivity {

    private Country mCurrent;
    private CountryRepository mCountryRepo;
    private Timer timer;
    private int mTimeElapsed;

    private String mUserGuess;
    private int mNoOfIncorrectGuesses;
    private boolean mIsTimed;

    private TextView mTvHint;
    private EditText mEtGuess;
    private TextView mTvGuessesRemaining;
    private Button mButtonGuess;
    private TextView mTvResult;
    private TextView mTvCorrectAnswer;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_hints);

        mTvHint = findViewById(R.id.guesshint_tv_hint);
        mEtGuess = findViewById(R.id.guesshint_et_guess);
        mTvGuessesRemaining = findViewById(R.id.guess_hint_tv_guesses_remaining);
        mButtonGuess = findViewById(R.id.guesshint_button_guess);
        mTvResult = findViewById(R.id.guesshint_tv_result);
        mTvCorrectAnswer = findViewById(R.id.guesshint_tv_correct_answer);
        mTimerProgressBar = findViewById(R.id.guesshint_pb_timer);
        mTimerText = findViewById(R.id.guesshint_tv_timer);

        mButtonGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guess();
            }
        });

        mTvGuessesRemaining.setText(String.format("Guesses Remaining: 3/3"));

        try {
            mCountryRepo = CountryRepository.getInstance(this);
        } catch (IOException|JSONException e) {
            e.printStackTrace();
        }

        mCurrent = mCountryRepo.getRandomCountry();
        mIsTimed = getIntent().getExtras().getBoolean("timer");

        setup();

    }

    private void setup(){
        setFlag();
        showTimerIfTimed();
        mUserGuess = mCurrent.getName().replaceAll("\\S","-");
        mTvHint.setText(mUserGuess);
    }

    private void setFlag(){
        ImageView mFlagImageView = findViewById(R.id.guesshint_image_flag);
        Drawable flag = mCountryRepo.getFlagForCountry(mCurrent);
        mFlagImageView.setImageDrawable(flag);
        mFlagImageView.setVisibility(View.VISIBLE);
    }

    private void guess(){

        char letter = '\u0000';
        if(mEtGuess.getText().toString().toLowerCase().length() != 0)
            letter = mEtGuess.getText().toString().toLowerCase().charAt(0);

        char[] correct_answer =  mCurrent.getName().toLowerCase().toCharArray();

        boolean guessedCorrect = false;
        for(int i =0; i<correct_answer.length; i++){
            if(correct_answer[i] == letter && mUserGuess.toCharArray()[i]=='-'){
                guessedCorrect = true;
                StringBuilder sb = new StringBuilder(mUserGuess);
                sb.setCharAt(i, letter);
                mUserGuess = sb.toString();
                mTvHint.setText(mUserGuess);
            }
        }

        if(!guessedCorrect){
            mNoOfIncorrectGuesses ++;
            mTvGuessesRemaining.setText(String.format("Guesses Remaining: %d / 3",3 - mNoOfIncorrectGuesses));
        }

        if(mUserGuess.equals(mCurrent.getName().toLowerCase()) || mNoOfIncorrectGuesses == 3){
            resetTimer();
            toggleSubmitButton();
            showResult();
        } else {
            resetTimer();
            showTimerIfTimed();
        }



    }

    private void next(){

        toggleSubmitButton();
        clearResult();

        mNoOfIncorrectGuesses = 0;
        mTvGuessesRemaining.setText("Guesses Remaining: 3/3");

        mCurrent = mCountryRepo.getRandomCountry();

        setup();
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
                            guess();
                        }
                    }
                });
            }
        },0,1000);
    }

    private void resetTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
            mTimeElapsed = 0;
        }
    }

    private void showResult(){
        if(mUserGuess.toLowerCase().equals(mCurrent.getName().toLowerCase())){
            mTvResult.setText("Your answer is correct");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            mTvResult.setText("Your answer is wrong");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mTvCorrectAnswer.setText("Correct answer is "+mCurrent.getName());
        }
    }

    private void clearResult(){
        mTvResult.setText("");
        mTvCorrectAnswer.setText("");
    }

    private void toggleSubmitButton(){
        switch (mButtonGuess.getText().toString()){
            case "Guess":
                mButtonGuess.setText("Next");
                mButtonGuess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        next();
                    }
                });
                break;
            case "Next":
                mButtonGuess.setText("Guess");
                mButtonGuess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guess();
                    }
                });
                break;

        }
    }

    private void showTimerIfTimed(){
        if(mIsTimed){
            mTimerProgressBar.setVisibility(View.VISIBLE);
            mTimerText.setVisibility(View.VISIBLE);
            runTimer();
        }
    }


}
