package com.ihandilnath.gameofflags;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Custom wrapper class around Java's in-built Timer object.
 * Provides convenient API for Timer functionality with support for invoking Controller callbacks on
 * Timer events like tick and timer expired.
 */
public class GameTimer extends Timer {

    private int mTimeElapsed; // time elapsed in seconds
    private CanTime mSubscriber; //Activity subscribing to Timer's events (tick and time expired)

    /**
     * Constructor
     * @param tm - Game Controller that CanTime
     */
    public GameTimer(CanTime tm){
        super();
        mSubscriber = tm;
    }

    /**
     * Getter for subscriber
     * @return - Game Controller subscribing to Timer's events
     */
    public CanTime getSubscriber() {
        return mSubscriber;
    }

    /**
     * Setter for subscriber
     * @param subscriber - Game Controller subscribing to Timer's events
     */
    public void setSubscriber(CanTime subscriber) {
        this.mSubscriber = subscriber;
    }

    /**
     * API method that starts timer. Schedules a 10sec GameTimerTask.
     */
    public void startTimer(){
        this.mTimeElapsed = 0;
        this.schedule(new GameTimerTask(), 0, 1000);
    }

    /**
     * API method that stop timer. Cancels any running threads and
     * removes any cancelled threads from thread queue
     */
    public void stopTimer(){
        this.cancel();
        this.purge();
    }

    /**
     * Class representing type of Custom TimerTask for GameTimer
     */
    class GameTimerTask extends TimerTask{
        @Override
        public void run() {
            GameTimer.this.mTimeElapsed++;
            mSubscriber.onTimeElapsed(mTimeElapsed);
            if(mTimeElapsed == 10){
                mSubscriber.onTimeExpired();
            }
        }
    }


}
