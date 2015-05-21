package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class ShootCommand implements Command {

    private static final String TAG = ShootCommand.class.getSimpleName();

    private RegulatedMotor regulatedMotorCenter;

    public ShootCommand(RegulatedMotor regulatedMotorCenter) {
        if (regulatedMotorCenter == null) {
            throw new NullPointerException(TAG + " motor cannot be null");
        }
        this.regulatedMotorCenter = regulatedMotorCenter;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");
        regulatedMotorCenter.rotateTo(1100);
        regulatedMotorCenter.resetTachoCount();
    }
}
