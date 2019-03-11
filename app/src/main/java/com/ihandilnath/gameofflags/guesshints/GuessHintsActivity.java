package com.ihandilnath.gameofflags.guesshints;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihandilnath.gameofflags.R;

public class GuessHintsActivity extends AppCompatActivity {

    private GuessHintsController controller;

    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;

    private TextView mTvHint;
    private EditText mEtGuess;
    private Button mSubmitButton;
    private TextView mTvGuessesRemaining;

    private TextView mTvResult;
    private TextView mTvCorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_hints);

        controller = new GuessHintsController(this, getIntent().getExtras().getBoolean("timer"));

        mTimerProgressBar = findViewById(R.id.guesshint_pb_timer);
        mTimerText = findViewById(R.id.guesshint_tv_timer);
        mTvHint = findViewById(R.id.guesshint_tv_hint);
        mEtGuess = findViewById(R.id.guesshint_et_guess);
        mSubmitButton = findViewById(R.id.guesshint_button_submit);
        mSubmitButton.setOnClickListener(view -> controller.guess());
        mTvGuessesRemaining = findViewById(R.id.guess_hint_tv_guesses_remaining);
        mTvResult = findViewById(R.id.guesshint_tv_result);
        mTvCorrectAnswer = findViewById(R.id.guesshint_tv_correct_answer);

        controller.setup();

    }

    public void setFlag(Drawable flag){
        ImageView mFlagImageView = findViewById(R.id.guesshint_image_flag);
        mFlagImageView.setImageDrawable(flag);
    }

    public char getUserGuess(){
        char letter = '\u0000';
        if(mEtGuess.getText().toString().toLowerCase().length() != 0)
            letter = mEtGuess.getText().toString().toLowerCase().charAt(0);
        return letter;
    }

    public void showResult(boolean isCorrect, String answer){
        if(isCorrect){
            mTvResult.setText("Your answer is correct");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            mTvResult.setText("Your answer is wrong");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mTvCorrectAnswer.setText("Correct answer is "+ answer);
        }
    }

    public void setHint(String hintText){
        mTvHint.setText(hintText);
    }

    public void toggleSubmitButton(){
        switch (mSubmitButton.getText().toString()){
            case "Submit":
                mSubmitButton.setText("Next");
                mSubmitButton.setOnClickListener(view -> recreate());
                break;
            case "Next":
                mSubmitButton.setText("Submit");
                mSubmitButton.setOnClickListener(view -> controller.guess());
                break;
        }
    }

    public void showTimer(){
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    public void updateTimer(int timeElapsed){
        mTimerText.setText(
                String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }

    public void setGuessesRemaining(int guessesRemaining) {
        mTvGuessesRemaining.setText(
                String.format("Guesses Remaining: %d/3",guessesRemaining));
    }

}
