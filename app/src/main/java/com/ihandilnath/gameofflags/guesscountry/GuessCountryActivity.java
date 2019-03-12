package com.ihandilnath.gameofflags.guesscountry;

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

import com.ihandilnath.gameofflags.R;

import java.util.List;

public class GuessCountryActivity extends AppCompatActivity {

    private GuessCountryController gameController;
    private Spinner mCountrySpinner;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private Button mSubmitButton;
    private TextView mTvResult;
    private TextView mTvAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_country);

        gameController = new GuessCountryController(this, getIntent().getExtras().getBoolean("timer"));

        // Creating required references to UI elements
        mTimerProgressBar = findViewById(R.id.guesscountry_pb_timer);
        mTimerText = findViewById(R.id.guesscountry_tv_timer);
        mSubmitButton = findViewById(R.id.guesscountry_button_submit);
        mTvResult = findViewById(R.id.guesscountry_tv_result);
        mTvAnswer = findViewById(R.id.guesscountry_tv_correct_answer);
        mSubmitButton.setOnClickListener(view -> submitAnswer());

        gameController.setup();

    }

    // UI methods
    public void setFlag(Drawable flag) {
        ImageView mFlagImageView = findViewById(R.id.guesscountry_image_flag);
        mFlagImageView.setImageDrawable(flag);
        mFlagImageView.setVisibility(View.VISIBLE);
    }

    public void populateCountries(List<String> countries) {
        mCountrySpinner = findViewById(R.id.guesscountry_spinner_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(adapter);
    }

    public void showTimer(){
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    public void submitAnswer(){
        String answer = ((String)mCountrySpinner.getSelectedItem());
        gameController.checkAnswer(answer);
    }

    public void showResult(boolean isAnswerCorrect, String answer){
        if(isAnswerCorrect){
            mTvResult.setText("Your answer is correct");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }else{
            mTvResult.setText("Your answer is wrong");
            mTvResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mTvAnswer.setText("Correct answer is "+answer);
        }
    }

    public void toggleSubmitButton(){
        switch (mSubmitButton.getText().toString()){
            case "Submit":
                mSubmitButton.setText("Next");
                mSubmitButton.setOnClickListener(view -> recreate());
                break;
            case "Next":
                mSubmitButton.setText("Submit");
                mSubmitButton.setOnClickListener(view -> submitAnswer());
                break;

        }
    }

    public void updateTimer(int timeElapsed) {
        mTimerText.setText(String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }

}
