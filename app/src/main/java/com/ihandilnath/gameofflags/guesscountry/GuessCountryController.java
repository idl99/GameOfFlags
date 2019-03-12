package com.ihandilnath.gameofflags.guesscountry;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.CanTime;

import org.json.JSONException;

import java.io.IOException;

/**
 * Class assigned single responsibility of performing game logic and controlling the view
 * for Guess Country game mode
 *
 * Implements CanTime interface to provide methods implementing Timer behavior for
 * Guess Country game mode on user request
 */
public class GuessCountryController implements CanTime {

    private final GuessCountryActivity mGameView; // Reference to view controlled by the Game Controller
    private final boolean mIsTimed; // boolean value indicating if the user has requested for timer
    private GameTimer mGameTimer; // Timer object to keep track of time
    private Country mRandomlySelectedCountry; // Array of randomly selected countries for game instance
    private CountryRepository mCountryRepo; // Reference to country repository which provides details of countries
                                            // like their name and corresponding drawable resource for country flag image

    /**
     * Constructor controller
     * @param gameView
     * @param isTimed
     */
    public GuessCountryController(GuessCountryActivity gameView, boolean isTimed) {
        this.mGameView = gameView;
        this.mIsTimed = isTimed;
        try {
            mCountryRepo = CountryRepository.getInstance(gameView);
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
        mGameView.runOnUiThread(()-> mGameView.submitAnswer());
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

        mRandomlySelectedCountry = mCountryRepo.getRandomCountry();

        mGameView.setFlag(mCountryRepo.getFlagForCountry(mRandomlySelectedCountry));
        mGameView.populateCountries(mCountryRepo.getAllCountryNames());

        if(mIsTimed){
            mGameView.showTimer();
            mGameTimer = new GameTimer(this);
            mGameTimer.startTimer();
        }

    }

    /**
     * Method which checks the answer given by user
     */
    public void checkAnswer(String input){

        if(mGameTimer !=null){
            mGameTimer.stopTimer();
        }

        if(input.equals(mRandomlySelectedCountry.getName())){
            mGameView.showResult(true, "");
        } else {
            mGameView.showResult(false, mRandomlySelectedCountry.getName());
        }

        mGameView.toggleSubmitButton();

    }

}
