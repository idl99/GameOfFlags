package com.ihandilnath.gameofflags;

/**
 * Interface declaring contract for all Game Controllers of game modes for which Timer can be activated
 */
public interface CanTime {

    /**
     * Callback method when time has elapsed, to update UI
     * @param timeElapsed
     */
    void onTimeElapsed(int timeElapsed);

    /**
     * Callback method to submit answer when time expires
     */
    void onTimeExpired();

}
