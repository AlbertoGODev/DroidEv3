package com.guerritastudio.albertogarcia.droidev3.model;


import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.command.*;

import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.BrickInfo;
import lejos.hardware.lcd.TextLCD;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class DroidEv3 extends RemoteRequestEV3 {

    private static final String TAG = DroidEv3.class.getSimpleName();

    //Para pruebas:
    public static final int MOTORS_SPEED = 5;
    private final int MOTOR_LEFT_SPEED = 1;
    private final int MOTOR_RIGHT_SPEED = 1;
    private float speedFactorLeft;
    private float speedFactorRight;


    private Audio audio;
    private TextLCD textLCD;

    private BrickInfo brickInfo;

    private RegulatedMotor regulatedMotorLeft, regulatedMotorRight;
    private CommandExecutor commandExecutor;

    public DroidEv3(BrickInfo brickInfo) throws IOException {
        super(brickInfo.getIPAddress());
        Log.d(TAG, "DroidEv3() constructor");
        this.brickInfo = brickInfo;
        initializeComponents();
    }

    private void initializeComponents() {
        audio = getAudio();
        initMotors();
        setMotorsSpeed();
        commandExecutor = CommandExecutor.getInstance();
        Log.d(TAG, "Components started correctly, ipAddress:" + brickInfo.getIPAddress() + " nameBrick: " + brickInfo.getName());
    }

    private void initMotors() {
        //L (for large), M (for medium) or G (for glide wheel)
        regulatedMotorLeft = createRegulatedMotor("B", 'L');
        regulatedMotorRight = createRegulatedMotor("C", 'L');
    }

    private void setMotorsSpeed() {
        //To regulatedMotorLeft:
        float maxSpeedLeft = regulatedMotorLeft.getMaxSpeed();
        speedFactorLeft = maxSpeedLeft / 100f;
        regulatedMotorLeft.setSpeed((int) (MOTOR_LEFT_SPEED * speedFactorLeft));

        Log.e(TAG, "setMotorsSpeed: maxSpeedLeft= " + maxSpeedLeft + "  ,speedFactorLeft= " + speedFactorLeft + " ,velSetted= " + MOTOR_LEFT_SPEED * speedFactorLeft);

        //To Right:
        float maxSpeedRight = regulatedMotorRight.getMaxSpeed();
        speedFactorRight = maxSpeedRight / 100f;
        regulatedMotorRight.setSpeed((int) (MOTOR_RIGHT_SPEED * speedFactorRight));

        Log.e(TAG, "setMotorsSpeed: maxSpeedRight= " + maxSpeedRight + "  ,speedFactorRight= " + speedFactorRight + " ,velSetted= " + MOTOR_RIGHT_SPEED * speedFactorRight);
    }

    public void setSpeed(int speed) {
        Log.d(TAG, "setSpeed ");
        int speedToLeft = (int) (speed * speedFactorLeft);
        int speedToRight = (int) (speed * speedFactorRight);

        commandExecutor.run(new SetSpeedCommand(regulatedMotorLeft, regulatedMotorRight, speedToLeft, speedToRight));
    }

    public BrickInfo getBrickInfo() {
        return brickInfo;
    }

    public void playBeep(int aCode) {
        audio.systemSound(aCode);
        /*
        aCode	Resulting Sound
        0	short beep
        1	double beep
        2	descending arpeggio
        3	ascending arpeggio
        4	long, low buzz
        */
    }


    //Move commands:

    public void stop() {
        Log.d(TAG, "stop ");
        commandExecutor.run(new StopCommand(regulatedMotorLeft, regulatedMotorRight));
    }

    public void moveForward(int speed) {
        Log.d(TAG, "moveForward ");
        commandExecutor.run(new GoForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) (speed * speedFactorRight)));
    }

    public void moveBackward(int speed) {
        Log.d(TAG, "moveBackward ");
        commandExecutor.run(new GoBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) (speed * speedFactorRight)));
    }

    public void moveLeft(int speed) {
        Log.d(TAG, "moveLeft ");
        commandExecutor.run(new GoLeftCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) (speed * speedFactorRight)));
    }

    public void moveRight(int speed) {
        Log.d(TAG, "moveRight ");
        commandExecutor.run(new GoRightCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) (speed * speedFactorRight)));
    }

    public void moveLeftForward(int speed) {
        Log.d(TAG, "moveLeftForward ");
        commandExecutor.run(new GoLeftForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed/3) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }

    public void moveRightForward(int speed) {
        Log.d(TAG, "moveRightForward ");
        commandExecutor.run(new GoRightForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed/3) * speedFactorRight)));

    }

    public void moveLeftBackward(int speed) {
        Log.d(TAG, "moveLeftBackward ");
        commandExecutor.run(new GoLeftBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed/3) * speedFactorRight)));

    }

    public void moveRightBackward(int speed) {
        Log.d(TAG, "moveRightBackward ");
        commandExecutor.run(new GoRightBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed/3) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }


    public void closeMotors() {
        Log.e(TAG, "closeMotors");
        if (regulatedMotorLeft != null || regulatedMotorRight != null) {
            regulatedMotorLeft.close();
            regulatedMotorRight.close();
        }
    }

}
