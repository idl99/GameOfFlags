package com.ihandilnath.gameofflags;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends Timer {

    private int timeElapsed;
    private CanTime subscriber;

    public GameTimer(CanTime tm){
        super();
        subscriber = tm;
    }

    public CanTime getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(CanTime subscriber) {
        this.subscriber = subscriber;
    }

    public void startTimer(){
        this.timeElapsed = 0;
        this.schedule(new GameTimerTask(), 0, 1000);
    }

    public void stopTimer(){
        this.cancel();
        this.purge();
    }

    class GameTimerTask extends TimerTask{
        @Override
        public void run() {
            GameTimer.this.timeElapsed++;
            subscriber.onTimeElapsed(timeElapsed);
            if(timeElapsed == 10){
                subscriber.onTimeExpired();
            }
        }
    }


}
