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

public class AdvancedLevelActivity extends AppCompatActivity {

    private AdvancedLevelController gameController;

    private EditText[] mInputFields;
    private Button mSubmitButton;
    private ImageView[] mFlagImageViews;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;
    private TextView mPointTally;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        gameController = new AdvancedLevelController(this,
                getIntent().getExtras().getBoolean("timer"));

        mInputFields = new EditText[]{findViewById(R.id.advancedlevel_et_input1),
                findViewById(R.id.advancedlevel_et_input2),
                findViewById(R.id.advancedlevel_et_input3)};

        mSubmitButton = findViewById(R.id.advancedlevel_button_submit);
        mSubmitButton.setOnClickListener(view -> submitAnswers());
        mFlagImageViews = new ImageView[]{findViewById(R.id.advancedlevel_image_flag_1),
                findViewById(R.id.advancedlevel_image_flag_2),
                findViewById(R.id.advancedlevel_image_flag_3)};
        mTimerProgressBar = findViewById(R.id.advancedlevel_pb_timer);
        mTimerText = findViewById(R.id.advancedlevel_tv_timer);
        mPointTally = findViewById(R.id.advancedlevel_tv_point_tally);

        gameController.setup();

    }

    public void setFlags(Drawable[] flags){
        for(int i=0; i<mFlagImageViews.length; i++){
            mFlagImageViews[i].setImageDrawable(flags[i]);
        }
    }

    public void submitAnswers(){
        String[] answers = new String[3];
        for(int i=0; i<mInputFields.length; i++){
            answers[i] = mInputFields[i].getText().toString();
        }
        gameController.checkAnswers(answers);
    }

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

    public void showResult(boolean[] areAllAnswersCorrect, String[] answers){
        for(int i=0; i< mInputFields.length; i++){
            if(!areAllAnswersCorrect[i]){
                mInputFields[i].setText(answers[i]);
                mInputFields[i].setTextColor(getResources().
                        getColor(R.color.colorPrimary));
            }
        }
    }


    public void updatePoints(int points){
        mPointTally.setText(String.format("Points: %d/3",points));
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

    private void clearInputFields(){
        for(EditText textField: mInputFields){
            textField.getText().clear();
        }
    }

}
