package com.ihandilnath.gameofflags.guesshints;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.TimeableMode;

import org.json.JSONException;

import java.io.IOException;

public class GuessHintsController implements TimeableMode {

    private final boolean isTimed;
    private GameTimer gameTimer;

    private final GuessHintsActivity gameView;

    private CountryRepository countryRepository;
    private Country answer;

    private String guessString;
    private int guessesRemaining;

    public GuessHintsController(GuessHintsActivity gameView, boolean isTimed) {
        this.gameView = gameView;
        this.isTimed = isTimed;
        try {
            this.countryRepository = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void setup(){

        guessesRemaining = 3;
        answer = countryRepository.getRandomCountry();
        guessString = answer.getName().replaceAll("\\S","-");

        gameView.setFlag(countryRepository.getFlagForCountry(answer));
        gameView.setHint(guessString);
        gameView.setGuessesRemaining(guessesRemaining);

        if(isTimed){
            gameView.showTimer();
            gameTimer = new GameTimer(this);
            gameTimer.startTimer();
        }

    }

    public void guess(){

        if(gameTimer != null){
            gameTimer.stopTimer();
        }

        char letter = gameView.getUserGuess();
        char[] correct_answer =  answer.getName().toLowerCase().toCharArray();

        boolean guessedCorrect = false;
        for(int i =0; i<correct_answer.length; i++){
            if(correct_answer[i] == letter && guessString.toCharArray()[i]=='-'){
                guessedCorrect = true;
                StringBuilder sb = new StringBuilder(guessString);
                sb.setCharAt(i, letter);
                guessString = sb.toString();
                gameView.setHint(guessString);
            }
        }

        if(!guessedCorrect){
            guessesRemaining--;
            gameView.setGuessesRemaining(guessesRemaining);
        }

        if(guessString.equals(answer.getName().toLowerCase()) || guessesRemaining == 0){
            gameView.toggleSubmitButton();
            gameView.showResult(guessString.equals(answer.getName().toLowerCase()), answer.getName());
        } else {
            gameTimer = new GameTimer(this);
            gameTimer.startTimer();
        }

    }

    public void next(){
        gameView.toggleSubmitButton();
        gameView.resetView();
        guessesRemaining = 0;
        setup();
    }

    @Override
    public void onTimeExpired() {
        gameView.runOnUiThread(this::guess);
    }

    @Override
    public void onTimeElapsed(final int timeElapsed) {
        gameView.runOnUiThread(() -> gameView.updateTimer(timeElapsed));
    }

}
