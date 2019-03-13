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

/**
 * Activity class for Guess Country game mode which encapsulates logic related to manipulating
 * the view.
 */
public class GuessCountryActivity extends AppCompatActivity {

    private GuessCountryController mGameController;
    private Spinner mCountrySpinner;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private Button mSubmitButton;
    private TextView mResultText;
    private TextView mAnswerText;

    /**
     * Android Lifecycle callback invoked when activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_country);

        mGameController = new GuessCountryController(this, getIntent().getExtras().getBoolean("timer"));

        // Creating references to UI elements
        mTimerProgressBar = findViewById(R.id.guesscountry_progressbar_timer);
        mTimerText = findViewById(R.id.guesscountry_text_timer);
        mSubmitButton = findViewById(R.id.guesscountry_button_submit);
        mResultText = findViewById(R.id.guesscountry_text_result);
        mAnswerText = findViewById(R.id.guesscountry_text_correct_answer);
        mSubmitButton.setOnClickListener(view -> submitAnswer());

        mGameController.setup();

    }

    /**
     * Method which sets Flag image
     * @param flag - Drawable resource of respective flag
     */
    public void setFlag(Drawable flag) {
        ImageView mFlagImageView = findViewById(R.id.guesscountry_image_flag);
        mFlagImageView.setImageDrawable(flag);
        mFlagImageView.setVisibility(View.VISIBLE);
    }

    /**
     * Method which populates values for the dropdown spinner which contains a list of all country names
     * from which the user will select the answer to submit.
     * @param countries
     */
    public void populateCountries(List<String> countries) {
        mCountrySpinner = findViewById(R.id.guesscountry_spinner_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(adapter);
    }

    /**
     * Method used to display Timer to user
     */
    public void showTimer(){
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    /**
     * Method which extract the answer given by the user and passes it to the controller
     * to check.
     */
    public void submitAnswer(){
        String answer = ((String)mCountrySpinner.getSelectedItem());
        mGameController.checkAnswer(answer);
    }

    /**
     * Method which displays result of game instance
     * @param isCorrect - is the answer given by user correct
     * @param answer - the correct answer
     */
    public void showResult(boolean isCorrect, String answer){
        if(isCorrect){
            mResultText.setText("CORRECT!");
            mResultText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }else{
            mResultText.setText("WRONG!");
            mResultText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mAnswerText.setText("Correct answer is "+answer);
        }
    }

    /**
     * Method used to toggle submit button, when user has completed the current game mode instance
     * and wishes to try guessing the country for a new flag
     */
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

    /**
     * Method used to update Timer in the event of time elapsed
     * @param timeElapsed
     */
    public void updateTimer(int timeElapsed) {
        mTimerText.setText(String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }

}
