package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class GoLeftCommand implements Command {

    private static final String TAG = GoLeftCommand.class.getSimpleName();

    private RegulatedMotor leftMotorRegulator;
    private RegulatedMotor rightMotorRegulator;
    //TODO TO TEST:
    int speedToLeft;
    int speedToRight;


    public GoLeftCommand(RegulatedMotor leftMotorRegulator, RegulatedMotor rightMotorRegulator, int speedToLeft, int speedToRight){
        if (leftMotorRegulator == null || rightMotorRegulator == null){
            throw new NullPointerException(TAG+" motor cannot be null");
        }
        this.leftMotorRegulator = leftMotorRegulator;
        this.rightMotorRegulator = rightMotorRegulator;

        //TODO TO TEST:
        this.speedToLeft = speedToLeft;
        this.speedToRight = speedToRight;
    }

    @Override
    public void run() {
        Log.e(TAG,"run()");

        leftMotorRegulator.setSpeed(speedToLeft);
        rightMotorRegulator.setSpeed(speedToRight);
        leftMotorRegulator.backward();
        rightMotorRegulator.forward();
    }
}
