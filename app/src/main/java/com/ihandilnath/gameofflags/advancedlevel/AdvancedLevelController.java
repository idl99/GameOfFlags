package com.ihandilnath.gameofflags.advancedlevel;

import android.graphics.drawable.Drawable;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.CanTime;

import org.json.JSONException;

import java.io.IOException;

/**
 * Class assigned single responsibility of performing game logic and controlling the view
 * for Advanced Level game mode
 *
 * Implements CanTime interface to provide methods implementing Timer behavior for
 * Advanced Level game mode on user request
 */
public class AdvancedLevelController implements CanTime {

    private final AdvancedLevelActivity mGameView; // Reference to view controlled by the Game Controller
    private final boolean mIsTimed; // boolean value indicating if the user has requested for timer
    private GameTimer mGameTimer; // Timer object to keep track of time
    private Country[] mRandomlySelectedCountries; // Array of randomly selected countries for game instance

    private CountryRepository mCountryRepo; // Reference to country repository which provides details of countries
                                        // like their name and corresponding drawable resource for country flag image

    private int mAttemptsLeft; // Number of attempts left
    private boolean[] mIsCorrectAnswerGiven; // Each index of array corresponds to if user had provided the correct
                                            // answer to the country at corresponding index of selected countries

    /**
     * Controller Constructor
     * @param gameView
     * @param isTimed
     */
    public AdvancedLevelController(AdvancedLevelActivity gameView, boolean isTimed) {
        this.mGameView = gameView;
        this.mIsTimed = isTimed;
        try {
            this.mCountryRepo = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        mRandomlySelectedCountries = new Country[3];
        mIsCorrectAnswerGiven = new boolean[3];
    }

    /**
     * Implementing abstract onTimeExpired callback method declared in the CanTime interface
     * for time expired event of Game Timer
     */
    @Override
    public void onTimeExpired() {
        mGameView.runOnUiThread(mGameView::submitAnswers);
    }

    /**
     * Implementing abstract onTimeElapsed callback method declared in the CanTime interface
     * for time elapsed event of Game Timer
     * @param timeElapsed - time elapsed in number of seconds
     */
    @Override
    public void onTimeElapsed(int timeElapsed) {
        mGameView.runOnUiThread(() -> mGameView.updateTimer(timeElapsed));
    }

    /**
     * Method which sets up game logic and view for game instance
     */
    public void setup(){

        mAttemptsLeft = 3;
        mIsCorrectAnswerGiven = new boolean[3];

        Drawable[] flags = new Drawable[3];

        for(int i=0; i<3; i++){
            Country rand = mCountryRepo.getRandomCountry();
            mRandomlySelectedCountries[i] = rand;
            flags[i] = mCountryRepo.getFlagForCountry(rand);
        }

        mGameView.setFlags(flags);

        if(mIsTimed){
            mGameView.showTimer();
            mGameTimer = new GameTimer(this);
            mGameTimer.startTimer();
        }

    }

    /**
     * Method which checks Answers given by user
     */
    public void checkAnswers(String[] input){

        if(mGameTimer != null){
            mGameTimer.stopTimer();
        }

        for(int i=0; i<input.length; i++){
            if(input[i].equalsIgnoreCase(mRandomlySelectedCountries[i].getName()))
                mIsCorrectAnswerGiven[i] = true;
        }

        mGameView.updateInputFieldState(mIsCorrectAnswerGiven);
        mGameView.updatePoints(noOfCorrectAnswers());

        if(!allAnsweredCorrect()){
            // User hasn't provided correct answer for all 3 flags
            mAttemptsLeft--;
            if(mAttemptsLeft == 0){
                mGameView.toggleSubmitButton();
                mGameView.showResult(mIsCorrectAnswerGiven, Country.getCountryNamesAsArray(
                        mRandomlySelectedCountries
                ));
            } else{
                // User has more attempts left
                mGameTimer = new GameTimer(this);
                mGameTimer.startTimer();
            }
        } else{
            // User has provided correct answer for all 3 flags
            mGameView.toggleSubmitButton();
        }

    }

    /**
     * Method which checks if all answers provided by user is correct
     * @return - boolean value indicating if all answers provided by user is correct
     */
    private boolean allAnsweredCorrect(){
        boolean allAnswerCorrect = true;
        for(boolean answer: mIsCorrectAnswerGiven){
            if(!answer)
                allAnswerCorrect = false;
        }
        return allAnswerCorrect;
    }

    /**
     * Method which counts the number of correct answers
     * @return - number of correct answers
     */
    private int noOfCorrectAnswers(){
        int noOfCorrectAnswers = 0;
        for(boolean answer: mIsCorrectAnswerGiven){
            if(answer)
                noOfCorrectAnswers++;
        }
        return noOfCorrectAnswers;
    }

}
