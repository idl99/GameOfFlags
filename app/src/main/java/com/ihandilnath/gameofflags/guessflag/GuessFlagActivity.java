package com.ihandilnath.gameofflags.guessflag;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihandilnath.gameofflags.R;

public class GuessFlagActivity extends AppCompatActivity {

    private GuessFlagController gameController;

    private ImageView[] mFlags;
    private TextView mResultTextView;
    private TextView mQuestionLabelTextView;
    private ProgressBar mTimerProgressBar;
    private TextView mTimerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_flag);

        mFlags = new ImageView[]{findViewById(R.id.guessflag_imageview_flag1),
                findViewById(R.id.guessflag_imageview_flag2),
                findViewById(R.id.guessflag_imageview_flag3)};

        mResultTextView = findViewById(R.id.guessflag_tv_result);
        mQuestionLabelTextView = findViewById(R.id.guessflag_question_label);
        Button mNextButton = findViewById(R.id.guessflag_next);
        mNextButton.setOnClickListener(view -> recreate());
        mTimerProgressBar = findViewById(R.id.guessflag_pb_timer);
        mTimerText = findViewById(R.id.guessflag_tv_timer);

        gameController = new GuessFlagController(this,
                getIntent().getExtras().getBoolean("timer"));

        gameController.setup();

    }

    public void setFlags(Drawable[] flags) {
        for(int i=0; i<flags.length; i++){
            mFlags[i].setImageDrawable(flags[i]);
        }
    }

    public void updateTimer(int timeElapsed) {
        mTimerText.setText(
                String.format("TIME LEFT: %ds",10 - timeElapsed));
        mTimerProgressBar.setProgress((int)((timeElapsed/10.0)*100.0));
    }

    public void showTimer() {
        mTimerProgressBar.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }


    public void showResult(boolean isAnswerCorrect) {
        if(isAnswerCorrect) {
            mResultTextView.setText("CORRECT");
            mResultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            mResultTextView.setText("WRONG");
            mResultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void flagClicked(View view) {
        Log.d("MY_TAG", "Flag clicked");
        switch(view.getId()){
            case R.id.guessflag_imageview_flag1:
                gameController.submit(0);
                break;
            case R.id.guessflag_imageview_flag2:
                gameController.submit(1);
                break;
            case R.id.guessflag_imageview_flag3:
                gameController.submit(2);
                break;
        }
    }

    public void setQuestionLabel(String name){
        mQuestionLabelTextView.setText("Select the flag of "+name);
    }

}
