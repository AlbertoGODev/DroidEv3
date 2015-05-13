package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class StopCommand implements Command {

    private static final String TAG = StopCommand.class.getSimpleName();

    private RegulatedMotor leftMotorRegulator;
    private RegulatedMotor rightMotorRegulator;


    public StopCommand(RegulatedMotor leftMotorRegulator, RegulatedMotor rightMotorRegulator){
        if (leftMotorRegulator == null || rightMotorRegulator == null){
            throw new NullPointerException(TAG+" motor cannot be null");
        }
        this.leftMotorRegulator = leftMotorRegulator;
        this.rightMotorRegulator = rightMotorRegulator;
    }

    @Override
    public void run() {
        Log.e(TAG,"run()");
        leftMotorRegulator.setSpeed(0);
        rightMotorRegulator.setSpeed(0);
        leftMotorRegulator.stop(true);
        rightMotorRegulator.stop(true);
    }
}
