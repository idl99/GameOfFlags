package com.ihandilnath.gameofflags.guessflag;

import android.graphics.drawable.Drawable;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.TimeableMode;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

public class GuessFlagController implements TimeableMode {

    private final boolean isTimed;
    private GameTimer gameTimer;

    private final GuessFlagActivity gameView;

    private CountryRepository countryRepository;
    private Country[] options = new Country[3];
    private int answerIndex;

    public GuessFlagController(GuessFlagActivity gameView, boolean isTimed) {
        this.gameView = gameView;
        this.isTimed = isTimed;
        try {
            this.countryRepository = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void setup(){

        Drawable[] flags = new Drawable[3];
        for(int i = 0; i < 3; i++){
            Country rand = countryRepository.getRandomCountry();
            options[i] = rand;
            flags[i] = countryRepository.getFlagForCountry(rand);
        }

        answerIndex = new Random().nextInt(3);

        gameView.setFlags(flags);

        gameView.setQuestionLabel(options[answerIndex].getName());

        if(isTimed){
            gameView.showTimer();
            gameTimer = new GameTimer(this);
            gameTimer.startTimer();
        }

    }

    public void submit(int selected){
        if(gameTimer != null){
            gameTimer.stopTimer();
        }

        if(selected  == answerIndex){
            // Answer is correct
            gameView.showResult(true);
        } else {
            // Answer is wrong
            gameView.showResult(false);
        }
    }

    @Override
    public void onTimeExpired() {
        gameView.runOnUiThread(()-> submit(-1));
    }

    @Override
    public void onTimeElapsed(int timeElapsed) {
        gameView.runOnUiThread(() -> gameView.updateTimer(timeElapsed));
    }
}
