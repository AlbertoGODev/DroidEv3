package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class GoRightForwardCommand implements Command {

    private static final String TAG = GoRightForwardCommand.class.getSimpleName();

    private RegulatedMotor leftMotorRegulator;
    private RegulatedMotor rightMotorRegulator;

    private int speedToLeft;
    private int speedToRight;

    public GoRightForwardCommand(RegulatedMotor leftMotorRegulator, RegulatedMotor rightMotorRegulator, int speedToLeft, int speedToRight) {
        if (leftMotorRegulator == null || rightMotorRegulator == null) {
            throw new NullPointerException(TAG + " motor cannot be null");
        }
        this.speedToLeft = speedToLeft;
        this.speedToRight = speedToRight;

        this.leftMotorRegulator = leftMotorRegulator;
        this.rightMotorRegulator = rightMotorRegulator;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");

        leftMotorRegulator.setSpeed(speedToLeft);
        rightMotorRegulator.setSpeed(speedToRight);

        leftMotorRegulator.forward();
        rightMotorRegulator.forward();
    }
}
