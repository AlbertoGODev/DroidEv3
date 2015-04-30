package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class SetSpeedCommand implements Command {

    private static final String TAG = SetSpeedCommand.class.getSimpleName();

    private RegulatedMotor leftMotorRegulator;
    private RegulatedMotor rightMotorRegulator;
    int speedToLeft;
    int speedToRight;

    public SetSpeedCommand(RegulatedMotor leftMotorRegulator, RegulatedMotor rightMotorRegulator, int speedToLeft, int speedToRight) {
        if (leftMotorRegulator == null || rightMotorRegulator == null) {
            throw new NullPointerException(TAG + " motor cannot be null");
        }
        this.leftMotorRegulator = leftMotorRegulator;
        this.rightMotorRegulator = rightMotorRegulator;
        this.speedToLeft = speedToLeft;
        this.speedToRight = speedToRight;

    }

    @Override
    public void run() {
        Log.e(TAG, "run()");

        Log.e(TAG, "speedToLeft = " + speedToLeft);
        leftMotorRegulator.setSpeed(speedToLeft);

        Log.e(TAG, "speedToRight = " + speedToRight);
        rightMotorRegulator.setSpeed(speedToRight);


/*
        //To Left:
        float maxSpeedLeft = leftMotorRegulator.getMaxSpeed();
        float speedFactorLeft = maxSpeedLeft / 100f;
        Log.e(TAG, "setMotorsSpeed: maxSpeedLeft= " + maxSpeedLeft + "  ,speedFactorLeft= " + speedFactorLeft + " ,speedToSet= " + speed +" ,velSetted= " + speed * speedFactorLeft);
        leftMotorRegulator.setSpeed((int) (speed * speedFactorLeft));

        //To Right:
        float maxSpeedRight = rightMotorRegulator.getMaxSpeed();
        float speedFactorRight = maxSpeedRight / 100f;
        Log.e(TAG, "setMotorsSpeed: maxSpeedRight= " + maxSpeedRight + "  ,speedFactorRight= " + speedFactorRight + " ,velSetted= " + speed * speedFactorRight);
        rightMotorRegulator.setSpeed((int) (speed * speedFactorRight));
    */
    }
}
