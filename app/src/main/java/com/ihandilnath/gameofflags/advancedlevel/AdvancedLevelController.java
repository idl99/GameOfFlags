package com.ihandilnath.gameofflags.advancedlevel;

import android.graphics.drawable.Drawable;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.TimeableMode;

import org.json.JSONException;

import java.io.IOException;

public class AdvancedLevelController implements TimeableMode {

    private final AdvancedLevelActivity gameView;
    private final boolean isTimed;
    private GameTimer gameTimer;

    // Array of countries displayed
    private Country[] answers;

    private CountryRepository countryRepo;
    private int attemptsLeft;

    // Each index of array corresponds to if user had provided the correct answer
    // to the country at corresponding index of answers
    private boolean[] isCorrectAnswerGiven;


    public AdvancedLevelController(AdvancedLevelActivity gameView, boolean isTimed) {
        this.gameView = gameView;
        this.isTimed = isTimed;
        try {
            this.countryRepo = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        answers = new Country[3];
        isCorrectAnswerGiven = new boolean[3];
    }

    public void setup(){

        attemptsLeft = 3;
        isCorrectAnswerGiven = new boolean[3];

        Drawable[] flags = new Drawable[3];

        for(int i=0; i<3; i++){
            Country rand = countryRepo.getRandomCountry();
            answers[i] = rand;
            flags[i] = countryRepo.getFlagForCountry(rand);
        }

        gameView.setFlags(flags);

        if(isTimed){
            gameView.showTimer();
            gameTimer = new GameTimer(this);
            gameTimer.startTimer();
        }

    }

    public void checkAnswers(String[] input){

        if(gameTimer != null){
            gameTimer.stopTimer();
        }

        for(int i=0; i<input.length; i++){
            if(input[i].equalsIgnoreCase(answers[i].getName()))
                isCorrectAnswerGiven[i] = true;
        }

        gameView.updateInputFieldState(isCorrectAnswerGiven);
        gameView.updatePoints(noOfCorrectAnswers());

        if(!allAnsweredCorrect()){
            attemptsLeft--;
            if(attemptsLeft == 0){
                gameView.toggleSubmitButton();
                gameView.showResult(isCorrectAnswerGiven, Country.getCountryNamesAsArray(
                        answers
                ));
            } else{
                gameTimer = new GameTimer(this);
                gameTimer.startTimer();
            }
        }

    }


    public void next(){
        gameView.toggleSubmitButton();
        gameView.resetView();
    }

    private boolean allAnsweredCorrect(){
        boolean allAnswerCorrect = true;
        for(boolean answer: isCorrectAnswerGiven){
            if(!answer)
                allAnswerCorrect = false;
        }
        return allAnswerCorrect;
    }

    private int noOfCorrectAnswers(){
        int noOfCorrectAnswers = 0;
        for(boolean answer: isCorrectAnswerGiven){
            if(answer)
                noOfCorrectAnswers++;
        }
        return noOfCorrectAnswers;
    }

    @Override
    public void onTimeExpired() {
        gameView.runOnUiThread(gameView::submitAnswers);
    }

    @Override
    public void onTimeElapsed(int timeElapsed) {
        gameView.runOnUiThread(() -> gameView.updateTimer(timeElapsed));
    }

}
