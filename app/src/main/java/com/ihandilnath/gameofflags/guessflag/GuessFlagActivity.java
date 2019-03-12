package com.ihandilnath.gameofflags.guessflag;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihandilnath.gameofflags.R;

/**
 * Activity class for Guess Flag game mode which encapsulates logic related to manipulating
 * the view.
 */
public class GuessFlagActivity extends AppCompatActivity {

    private GuessFlagController mGameController;
    private ImageView[] mFlagImages;
    private TextView mResultText;
    private TextView mQuestionLabelText;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;

    /**
     * Android Lifecycle callback invoked when activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_flag);

        mGameController = new GuessFlagController(this,
                getIntent().getExtras().getBoolean("timer"));

        // Creating references to UI elements
        mFlagImages = new ImageView[]{findViewById(R.id.guessflag_image_flag1),
                findViewById(R.id.guessflag_image_flag2),
                findViewById(R.id.guessflag_image_flag3)};

        mResultText = findViewById(R.id.guessflag_text_result);
        mQuestionLabelText = findViewById(R.id.guessflag_text_questionlabel);

        Button mNextButton = findViewById(R.id.guessflag_next);
        mNextButton.setOnClickListener(view -> recreate());

        mTimerProgressBar = findViewById(R.id.guessflag_progressbar_timer);
        mTimerText = findViewById(R.id.guessflag_text_timer);

        mGameController.setup();

    }

    /**
     * Method which sets images of Flags
     * @param flags - array of Drawable resources of flags
     */
    public void setFlags(Drawable[] flags) {
        for(int i=0; i<flags.length; i++){
            mFlagImages[i].setImageDrawable(flags[i]);
        }
    }

    /**
     * On Click event handler for Flag images.
     * Submits user's answer to the Game Controller to check answer
     * @param view
     */
    public void flagClicked(View view) {
        switch(view.getId()){
            case R.id.guessflag_image_flag1:
                mGameController.checkAnswer(0);
                break;
            case R.id.guessflag_image_flag2:
                mGameController.checkAnswer(1);
                break;
            case R.id.guessflag_image_flag3:
                mGameController.checkAnswer(2);
                break;
        }
    }

    /**
     * Method which displays result of the game instance
     * @param isAnswerCorrect - boolean value indicating if user's answer is correct
     */
    public void showResult(boolean isAnswerCorrect) {
        if(isAnswerCorrect) {
            mResultText.setText("CORRECT");
            mResultText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            mResultText.setText("WRONG");
            mResultText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    /**
     * Method used to display Timer to user
     */
    public void showTimer() {
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    /**
     * Method used to update Timer in the event of time elapsed
     * @param timeElapsed
     */
    public void updateTimer(int timeElapsed) {
        mTimerText.setText(
                String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }


    /**
     * Method which sets the Question Label to indicate the name of country for
     * which user has to select the correct, corresponding flag
     * @param name
     */
    public void setQuestionLabel(String name){
        mQuestionLabelText.setText("Select the flag of "+name);
    }

}
