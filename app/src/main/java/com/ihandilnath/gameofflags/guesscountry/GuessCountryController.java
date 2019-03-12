package com.ihandilnath.gameofflags.guesscountry;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.CanTime;

import org.json.JSONException;

import java.io.IOException;

public class GuessCountryController implements CanTime {

    private GuessCountryActivity gameView;
    private boolean isTimed;
    private GameTimer gameTimer;

    private Country answer;
    private CountryRepository countryRepo;

    public GuessCountryController(GuessCountryActivity gameView, boolean isTimed) {
        this.gameView = gameView;
        this.isTimed = isTimed;
        try {
            countryRepo = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void setup(){
        answer = countryRepo.getRandomCountry();

        gameView.setFlag(countryRepo.getFlagForCountry(answer));
        gameView.populateCountries(countryRepo.getAllCountryNames());

        if(isTimed){
            gameView.showTimer();
            gameTimer = new GameTimer(this);
            gameTimer.startTimer();
        }

    }

    public void checkAnswer(String input){

        if(gameTimer!=null){
            gameTimer.stopTimer();
        }

        if(input.equals(answer.getName())){
            gameView.showResult(true, "");
        } else {
            gameView.showResult(false, answer.getName());
        }

        gameView.toggleSubmitButton();
    }

    @Override
    public void onTimeExpired() {
        gameView.runOnUiThread(()-> gameView.submitAnswer());
    }

    @Override
    public void onTimeElapsed(int timeElapsed) {
        gameView.runOnUiThread(() -> gameView.updateTimer(timeElapsed));
    }
}
