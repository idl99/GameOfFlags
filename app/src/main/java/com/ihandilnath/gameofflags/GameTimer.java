package com.ihandilnath.gameofflags;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends Timer {

    private int timeElapsed;
    private TimeableMode subscriber;

    public GameTimer(TimeableMode tm){
        super();
        subscriber = tm;
    }

    public TimeableMode getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(TimeableMode subscriber) {
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
            ((Activity)GameTimer.this.subscriber).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GameTimer.this.timeElapsed++;
                    subscriber.onTimeElapsed(GameTimer.this.timeElapsed);
                    if(GameTimer.this.timeElapsed == 10){
                        subscriber.onTimeExpired();
                    }
                }
            });
        }
    }

}
