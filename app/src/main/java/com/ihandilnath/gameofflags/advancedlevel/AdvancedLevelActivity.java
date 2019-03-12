package com.ihandilnath.gameofflags.advancedlevel;

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
 * Activity class for Advanced Level game mode which encapsulates logic related to manipulating
 * the view.
 */
public class AdvancedLevelActivity extends AppCompatActivity {

    private AdvancedLevelController mGameController;
    private EditText[] mInputFields;
    private Button mSubmitButton;
    private ImageView[] mFlagImageViews;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private TextView mPointTallyText;

    /**
     * Android Lifecycle callback invoked when activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        mGameController = new AdvancedLevelController(this,getIntent().getExtras().getBoolean("timer"));

        // Creating required references to UI elements
        mInputFields = new EditText[]{findViewById(R.id.advancedlevel_edittext_input1),
                findViewById(R.id.advancedlevel_edittext_input2), findViewById(R.id.advancedlevel_edittext_input3)};
        mSubmitButton = findViewById(R.id.advancedlevel_button_submit);
        mSubmitButton.setOnClickListener(view -> submitAnswers());
        mFlagImageViews = new ImageView[]{findViewById(R.id.advancedlevel_image_flag1),
                findViewById(R.id.advancedlevel_image_flag2), findViewById(R.id.advancedlevel_image_flag3)};
        mTimerProgressBar = findViewById(R.id.advancedlevel_progressbar_timer);
        mTimerText = findViewById(R.id.advancedlevel_text_timer);
        mPointTallyText = findViewById(R.id.advancedlevel_text_pointtally);

        mGameController.setup();

    }

    /**
     * Method which sets images of Flags
     * @param flags - array of Drawable resources of flags
     */
    public void setFlags(Drawable[] flags){
        for(int i=0; i<mFlagImageViews.length; i++){
            mFlagImageViews[i].setImageDrawable(flags[i]);
        }
    }

    /**
     * Method which extracts user's answers from Edit Text fields and sends to GameController
     * to check answers.
     */
    public void submitAnswers(){
        String[] answers = new String[3];
        for(int i=0; i<mInputFields.length; i++){
            answers[i] = mInputFields[i].getText().toString();
        }
        mGameController.checkAnswers(answers);
    }

    /**
     * Method which updates Input field state to indicate in green color if answer provided was correct or
     * in red color if answer given was incorrect. State of all three flag Input fields are updated.
     * @param isCorrectAnswerGiven - boolean array which corresponds to if the user has provided the correct
     *                             answer to each Flag
     */
    public void updateInputFieldState(boolean[] isCorrectAnswerGiven){
        for(int i=0; i<isCorrectAnswerGiven.length; i++){
            if(isCorrectAnswerGiven[i]){
                mInputFields[i].setTextColor(getResources().getColor(android.R.color.holo_green_light));
                mInputFields[i].setEnabled(false);
            } else{
                mInputFields[i].setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }
    }

    /**
     * Method which displays result of game
     * @param isCorrectAnswerGiven - boolean array which corresponds to if the user has provided the correct
     *                                   answer to each Flag
     */
    public void showResult(boolean[] isCorrectAnswerGiven, String[] answers){
        for(int i=0; i< mInputFields.length; i++){
            if(!isCorrectAnswerGiven[i]){
                mInputFields[i].setText(answers[i]);
                mInputFields[i].setTextColor(getResources().
                        getColor(R.color.colorPrimary));
            }
        }
    }

    /**
     * Method which updates the point tally in the game view
     * @param points
     */
    public void updatePoints(int points){
        mPointTallyText.setText(String.format("Points: %d/3",points));
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
     * Method used to toggle submit button, when user has completed the current game mode instance
     * and wishes to try advanced level for a new set of flags
     */
    public void toggleSubmitButton(){
        switch (mSubmitButton.getText().toString()){
            case "Submit":
                mSubmitButton.setText("Next");
                mSubmitButton.setOnClickListener(view -> {
                    clearInputFields();
                    recreate();
                });
                break;
            case "Next":
                mSubmitButton.setText("Submit");
                mSubmitButton.setOnClickListener(view -> submitAnswers());
                break;
        }
    }

    /**
     * Method used to clear input given by user on Edit Text field
     */
    private void clearInputFields(){
        for(EditText textField: mInputFields){
            textField.getText().clear();
        }
    }

}
