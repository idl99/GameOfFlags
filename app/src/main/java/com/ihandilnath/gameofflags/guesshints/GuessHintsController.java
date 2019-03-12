package com.ihandilnath.gameofflags.guesshints;

import com.ihandilnath.gameofflags.Country;
import com.ihandilnath.gameofflags.CountryRepository;
import com.ihandilnath.gameofflags.GameTimer;
import com.ihandilnath.gameofflags.CanTime;

import org.json.JSONException;

import java.io.IOException;

/**
 * Class assigned single responsibility of performing game logic and controlling the view
 * for Guess Hints game mode
 *
 * Implements CanTime interface to provide methods implementing Timer behavior for
 * Guess Hints game mode on user request
 */
public class GuessHintsController implements CanTime {

    private final GuessHintsActivity mGameView; // Reference to view controlled by the Game Controller
    private final boolean mIsTimed; // boolean value indicating if the user has requested for timer
    private GameTimer mGameTimer; // Timer object to keep track of time
    private CountryRepository mCountryRepository; // Reference to country repository which provides details of countries
    // like their name and corresponding drawable resource for country flag image
    private Country mRandomlySelectedCountry;
    private String mUserGuess;
    private int mAttemptsLeft;

    /**
     * Controller constructor
     * @param gameView
     * @param isTimed
     */
    public GuessHintsController(GuessHintsActivity gameView, boolean isTimed) {
        this.mGameView = gameView;
        this.mIsTimed = isTimed;
        try {
            this.mCountryRepository = CountryRepository.getInstance(gameView);
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
        mGameView.runOnUiThread(this::checkGuess);
    }

    /**
     * Implementing abstract onTimeElapsed callback method declared in the CanTime interface
     * for time elapsed event of Game Timer
     * @param timeElapsed - time elapsed in number of seconds
     */
    @Override
    public void onTimeElapsed(final int timeElapsed) {
        mGameView.runOnUiThread(() -> mGameView.updateTimer(timeElapsed));
    }

    /**
     * Method which sets up game logic and view for game instance
     */
    public void setup(){

        mAttemptsLeft = 3;
        mRandomlySelectedCountry = mCountryRepository.getRandomCountry();
        mUserGuess = mRandomlySelectedCountry.getName().replaceAll("\\S","-");

        mGameView.setFlag(mCountryRepository.getFlagForCountry(mRandomlySelectedCountry));
        mGameView.setHint(mUserGuess);
        mGameView.setGuessesRemaining(mAttemptsLeft);

        if(mIsTimed){
            mGameView.showTimer();
            mGameTimer = new GameTimer(this);
            mGameTimer.startTimer();
        }

    }

    /**
     * Method which checks user guess
     */
    public void checkGuess(){

        if(mGameTimer != null){
            mGameTimer.stopTimer();
        }

        char letter = mGameView.getUserGuess();
        char[] correct_answer =  mRandomlySelectedCountry.getName().toLowerCase().toCharArray();

        boolean guessedCorrect = false;
        for(int i =0; i<correct_answer.length; i++){
            if(correct_answer[i] == letter && mUserGuess.toCharArray()[i]=='-'){
                guessedCorrect = true;
                StringBuilder sb = new StringBuilder(mUserGuess);
                sb.setCharAt(i, letter);
                mUserGuess = sb.toString();
                mGameView.setHint(mUserGuess);
            }
        }

        if(!guessedCorrect){
            // User hasn't correctly guessed a letter
            mAttemptsLeft--;
            mGameView.setGuessesRemaining(mAttemptsLeft);
        }

        if(mUserGuess.equals(mRandomlySelectedCountry.getName().toLowerCase()) || mAttemptsLeft == 0){
            // User has solved the hints and provided answer or run out of attempts
            mGameView.toggleSubmitButton();
            mGameView.showResult(mUserGuess.equals(mRandomlySelectedCountry.getName().toLowerCase()), mRandomlySelectedCountry.getName());
        } else {
            // Reinitialize timer for next user guess
            mGameTimer = new GameTimer(this);
            mGameTimer.startTimer();
        }

    }

}
