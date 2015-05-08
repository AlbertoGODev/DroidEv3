package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;

import lejos.hardware.LED;
import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class SetLedPatternCommand implements Command {

    private static final String TAG = SetLedPatternCommand.class.getSimpleName();

    private LED led;
    private int pattern;

    public SetLedPatternCommand(LED led, int pattern) {
        this.led = led;
        this.pattern = pattern;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");

        led.setPattern(pattern);
        DroidEv3.lastPattern = pattern;
    }
}
