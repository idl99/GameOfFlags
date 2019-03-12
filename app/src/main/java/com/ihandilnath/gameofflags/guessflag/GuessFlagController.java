package com.ihandilnath.gameofflags.guessflag;

import android.graphics.drawable.Drawable;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.CanTime;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

/**
 * Class assigned single responsibility of performing game logic and controlling the view
 * for Guess Flag game mode
 *
 * Implements CanTime interface to provide methods implementing Timer behavior for
 * Guess Flag game mode on user request
 */
public class GuessFlagController implements CanTime {

    private final GuessFlagActivity mGameView; // Reference to view controlled by the Game Controller
    private final boolean mIsTimed; // boolean value indicating if the user has requested for timer
    private GameTimer mGameTimer; // Timer object to keep track of time
    private CountryRepository mCountryRepo; // Reference to country repository which provides details of countries
    // like their name and corresponding drawable resource for country flag image

    private Country[] mRandomlySelectedCountries = new Country[3]; // Array of randomly selected countries for game instance
    private int answerIndex; // Index at which correct answer can be found on array of randomly selected countries

    /**
     * Controller constructor
     * @param gameView
     * @param isTimed
     */
    public GuessFlagController(GuessFlagActivity gameView, boolean isTimed) {
        this.mGameView = gameView;
        this.mIsTimed = isTimed;
        try {
            this.mCountryRepo = CountryRepository.getInstance(gameView);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementing abstract onTimeExpired callback method declared in the CanTime interface
     * for time expired event of Game Timer
     */
    @Override
    public void onTimeExpired() {
        mGameView.runOnUiThread(()-> checkAnswer(-1));
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

        Drawable[] flags = new Drawable[3];
        for(int i = 0; i < 3; i++){
            Country rand = mCountryRepo.getRandomCountry();
            mRandomlySelectedCountries[i] = rand;
            flags[i] = mCountryRepo.getFlagForCountry(rand);
        }

        answerIndex = new Random().nextInt(3);

        mGameView.setFlags(flags);

        mGameView.setQuestionLabel(mRandomlySelectedCountries[answerIndex].getName());

        if(mIsTimed){
            mGameView.showTimer();
            mGameTimer = new GameTimer(this);
            mGameTimer.startTimer();
        }

    }

    /**
     * Method which check answer submitted by user
     */
    public void checkAnswer(int selected){
        if(mGameTimer != null){
            mGameTimer.stopTimer();
        }

        if(selected  == answerIndex){
            // Answer is correct
            mGameView.showResult(true);
        } else {
            // Answer is wrong
            mGameView.showResult(false);
        }
    }

}
