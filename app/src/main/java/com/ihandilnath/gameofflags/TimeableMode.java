package com.ihandilnath.gameofflags;

public interface TimeableMode {

    // Method to execute when timer has expired
    void onTimeExpired();

    // Method to execute when time has elapsed (updating UI)
    void onTimeElapsed(int timeElapsed);

}
