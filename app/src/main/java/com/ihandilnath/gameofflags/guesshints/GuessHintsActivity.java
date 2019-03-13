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

/**
 * Activity class for Guess Hints game mode which encapsulates logic related to manipulating
 * the view.
 */
public class GuessHintsActivity extends AppCompatActivity {

    private GuessHintsController mGameController;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private TextView mHintText;
    private EditText mGuessEditText;
    private Button mSubmitButton;
    private TextView mGuessesRemainingText;
    private TextView mResultText;
    private TextView mCorrectAnswerText;

    /**
     * Android Lifecycle callback invoked when activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_hints);

        mGameController = new GuessHintsController(this,
                getIntent().getExtras().getBoolean("timer"));

        // Creating references to UI elements
        mTimerProgressBar = findViewById(R.id.guesshint_progressbar_timer);
        mTimerText = findViewById(R.id.guesshint_text_timer);
        mHintText = findViewById(R.id.guesshint_text_hint);
        mGuessEditText = findViewById(R.id.guesshint_edittext_guess);
        mSubmitButton = findViewById(R.id.guesshint_button_submit);
        mSubmitButton.setOnClickListener(view -> mGameController.checkGuess());
        mGuessesRemainingText = findViewById(R.id.guesshint_text_guessesremaining);
        mResultText = findViewById(R.id.guesshint_text_result);
        mCorrectAnswerText = findViewById(R.id.guesshint_text_correctanswer);

        mGameController.setup();

    }

    /**
     * Method which sets Flag image
     * @param flag - Drawable resource of respective flag
     */
    public void setFlag(Drawable flag){
        ImageView mFlagImageView = findViewById(R.id.guesshint_image_flag);
        mFlagImageView.setImageDrawable(flag);
    }

    /**
     * Getter method to get User's guess from Edit Text input control
     * @return - letter guessed by user
     */
    public char getUserGuess(){
        char letter = '\u0000';
        if(mGuessEditText.getText().toString().toLowerCase().length() != 0)
            letter = mGuessEditText.getText().toString().toLowerCase().charAt(0);
        return letter;
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
        } else {
            mResultText.setText("WRONG!");
            mResultText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            mCorrectAnswerText.setText("Correct answer is "+ answer);
        }
    }

    /**
     * Method use to set country name on Hint Text View.
     * @param hintText - name of country to be guessed by user
     */
    public void setHint(String hintText){
        mHintText.setText(hintText);
    }

    /**
     * Method used to display Timer to user
     */
    public void showTimer(){
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    /**
     * Method used to update Timer in the event of time elapsed
     * @param timeElapsed
     */
    public void updateTimer(int timeElapsed){
        mTimerText.setText(
                String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }

    /**
     * Method used to display to user number of remaining guesses
     * @param guessesRemaining
     */
    public void setGuessesRemaining(int guessesRemaining) {
        mGuessesRemainingText.setText(
                String.format("Guesses Remaining: %d/3",guessesRemaining));
    }

    /**
     * Method used to toggle submit button, when user has completed the current game mode instance
     * and wishes to try guessing the hint for a new flag
     */
    public void toggleSubmitButton(){
        switch (mSubmitButton.getText().toString()){
            case "Submit":
                mSubmitButton.setText("Next");
                mSubmitButton.setOnClickListener(view -> recreate());
                break;
            case "Next":
                mSubmitButton.setText("Submit");
                mSubmitButton.setOnClickListener(view -> mGameController.checkGuess());
                break;
        }
    }

}
