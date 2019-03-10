package com.ihandilnath.gameofflags;

public interface TimeableMode {

    // Method to execute when timer has expired
    public void onTimeExpired();

    // Method to execute when time has elapsed (updating UI)
    public void onTimeElapsed(int timeElapsed);

}
