package com.ihandilnath.gameofflags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.ihandilnath.gameofflags.advancedlevel.AdvancedLevelActivity;
import com.ihandilnath.gameofflags.guesscountry.GuessCountryActivity;
import com.ihandilnath.gameofflags.guessflag.GuessFlagActivity;
import com.ihandilnath.gameofflags.guesshints.GuessHintsActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * Android Lifecycle callback invoked when activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method which navigates to user selected game mode.
     * Invoked on click event of game mode buttons.
     * @param view
     */
    public void startGameMode(View view) {

        Switch mSwitchTimer = findViewById(R.id.home_switch_timer);
        int selectedGameMode = view.getId(); // Getting the ID of the button corresponding
                                            // to the game mode selected by the user

        Intent intent = null;

        switch(selectedGameMode){
            // Checking which button user clicked on
            case R.id.home_button_guesscountry:
                // User wishes to enter "Guess the Country" game mode
                intent = new Intent(this, GuessCountryActivity.class);
                break;
            case R.id.home_button_guesshints:
                // User wishes to enter "Guess Hints" game mode
                intent = new Intent(this, GuessHintsActivity.class);
                break;
            case R.id.home_button_guessflag:
                // User wishes to enter "Guess the Flag" game mode
                intent = new Intent(this, GuessFlagActivity.class);
                break;
            case R.id.home_button_advancedlevel:
                // User wishes to enter "Advanced level" game mode
                intent = new Intent(this, AdvancedLevelActivity.class);
                break;
        }

        intent.putExtra("timer", mSwitchTimer.isChecked()); // Pass extra indicating if user
                                                                // has requested for timer
        startActivity(intent);

    }

}
