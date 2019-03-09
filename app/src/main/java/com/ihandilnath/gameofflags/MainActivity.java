package com.ihandilnath.gameofflags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGameMode(View view) {

        Switch mSwitchTimer = findViewById(R.id.switch_timer);

        int selectedGameMode = view.getId(); // Getting the ID of the button corresponding
                                            // to the game mode selected by the user

        Intent intent = null;

        switch(selectedGameMode){
            // Checking which button user clicked on
            case R.id.button_guess_country:
                // User wishes to start Guess the Country game mode
                intent = new Intent(this, GuessCountryActivity.class);
                break;
            case R.id.button_guess_hints:
                // User wishes to start Guess Hints game mode
                intent = new Intent(this, GuessHintsActivity.class);
                break;
            case R.id.button_guess_flag:
                // User wishes to start Guess the Flag game mode
                intent = new Intent(this, GuessFlagActivity.class);
                break;
            case R.id.button_advanced_level:
                // User wishes to start Advanced level game mode
                intent = new Intent(this, AdvancedLevelActivity.class);
                break;
        }

        intent.putExtra("timer", mSwitchTimer.isChecked());
        startActivity(intent);

    }

}
