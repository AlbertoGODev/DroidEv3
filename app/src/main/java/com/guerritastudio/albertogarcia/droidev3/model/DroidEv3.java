package com.guerritastudio.albertogarcia.droidev3.model;


import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.command.*;

import java.io.File;
import java.io.IOException;

import lejos.hardware.BrickInfo;
import lejos.hardware.LED;
import lejos.hardware.Power;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.remote.ev3.RemoteRequestSampleProvider;
import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class DroidEv3 extends RemoteRequestEV3 {

    private static final String TAG = DroidEv3.class.getSimpleName();

    public static final int MY_DEFAULT_SPEED = 10;
    private static final int DEFAULT_SHOOT_SPEED = 100;

    private float speedFactorLeft;
    private float speedFactorRight;
    private float speedFactorCenter;

    private BrickInfo brickInfo;
    private String ipAddress;
    private Power power;
    private LED led;
    public static int lastLedPattern = 0;

    RemoteRequestSampleProvider touchSensorSP;
    RemoteRequestSampleProvider irSensorSP;
    RemoteRequestSampleProvider colorSP;

    private RegulatedMotor regulatedMotorLeft, regulatedMotorRight, regulatedMotorCenter;
    private CommandExecutor commandExecutor;


    public DroidEv3(BrickInfo brickInfo) throws IOException {
        super(brickInfo.getIPAddress());
        Log.d(TAG, "DroidEv3() constructor");
        this.brickInfo = brickInfo;
        initializeComponents();
    }

    public DroidEv3(String ip) throws IOException {
        super(ip);
        Log.d(TAG, "DroidEv3(ip) constructor");
    }

    private void initializeComponents() {
        ipAddress = brickInfo.getIPAddress();
        commandExecutor = CommandExecutor.getInstance();
        //openMotors();//Mirar de poner en onCreate del fragment Joystick.
        //setMotorsSpeed();
        Log.d(TAG, "Components started correctly, ipAddress:" + ipAddress + " nameBrick: " + brickInfo.getName());
    }

    //BrickInfo:
    public BrickInfo getBrickInfo() {
        return brickInfo;
    }

    //PlaySample:
    public void playSpeech(File file) {
        Log.d(TAG, "playSpeech() file name = " + file.getName());
        commandExecutor.run(new AudioCommand(this, file));
    }

    //UploadFile:
    public void uploadFile(File file) {
        Log.d(TAG, "uploadFile() file name = " + file.getName());
        commandExecutor.run(new UploadFileCommand(this, ipAddress, file));
    }

    //Led Control:
    public void initLed() {
        Log.d(TAG, "initLed()");
        led = getLED();
    }

    public void setLEDPattern(int pattern) {
        Log.d(TAG, "setLEDPattern with pattern = " + pattern + " and lastLedPattern = " + lastLedPattern);
        if (pattern != lastLedPattern) {
            commandExecutor.run(new SetLedPatternCommand(led, pattern));
        }
    }

    //Power Info:
    public void initPower() {
        Log.d(TAG, "initPower");
        power = getPower();
    }

    //Sensors:
    public void openSensors() {
        try {
            Log.d(TAG, "openSensors()");
            touchSensorSP = (RemoteRequestSampleProvider) createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", "Touch");
            Log.e(TAG, "touchSensorSP opened");
            irSensorSP = (RemoteRequestSampleProvider) createSampleProvider("S4", "lejos.hardware.sensor.EV3IRSensor", "Distance");
            Log.e(TAG, "irSensorSP opened");
            colorSP = (RemoteRequestSampleProvider) createSampleProvider("S3", "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
            Log.e(TAG, "colorSP opened");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSensors() {
        Log.d(TAG, "closeSensors()");
        try {
            if (touchSensorSP != null) {
                touchSensorSP.close();
            }
            if (irSensorSP != null) {
                irSensorSP.close();
            }
            if (colorSP != null) {
                colorSP.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchSensorsInfo(GetSensorsInfoCommand.OnPowerInfo callback) {
        Log.d(TAG, "fetchSensorsInfo()");
        commandExecutor.run(new GetSensorsInfoCommand(power, callback, touchSensorSP, irSensorSP, colorSP));
    }

    //Motors:

    public void openMotors() {
        //L (for large), M (for medium) or G (for glide wheel)
        regulatedMotorLeft = createRegulatedMotor("B", 'L');
        regulatedMotorRight = createRegulatedMotor("C", 'L');
        regulatedMotorCenter = createRegulatedMotor("A", 'L');
    }

    public void setMotorsSpeed() {
        //To regulatedMotorLeft:
        float maxSpeedLeft = regulatedMotorLeft.getMaxSpeed();
        speedFactorLeft = maxSpeedLeft / 100f;
        regulatedMotorLeft.setSpeed((int) (MY_DEFAULT_SPEED * speedFactorLeft));

        Log.e(TAG, "setMotorsSpeed: maxSpeedLeft= " + maxSpeedLeft + "  ,speedFactorLeft= " + speedFactorLeft + " ,velSetted= " + MY_DEFAULT_SPEED * speedFactorLeft);

        //To regulatedMotorRight:
        float maxSpeedRight = regulatedMotorRight.getMaxSpeed();
        speedFactorRight = maxSpeedRight / 100f;
        regulatedMotorRight.setSpeed((int) (MY_DEFAULT_SPEED * speedFactorRight));

        Log.e(TAG, "setMotorsSpeed: maxSpeedRight= " + maxSpeedRight + "  ,speedFactorRight= " + speedFactorRight + " ,velSetted= " + MY_DEFAULT_SPEED * speedFactorRight);

        //To regulatedMotorCenter:
        float maxSpeedCenter = regulatedMotorCenter.getMaxSpeed();
        speedFactorCenter = maxSpeedCenter / 100f;
        regulatedMotorCenter.setSpeed((int) (DEFAULT_SHOOT_SPEED * speedFactorCenter));

        Log.e(TAG, "setMotorsSpeed: maxSpeedCenter= " + maxSpeedCenter + "  ,speedFactorCenter= " + speedFactorCenter + " ,velSetted= " + DEFAULT_SHOOT_SPEED * speedFactorCenter);
    }

    public void closeMotors() {
        try {
            regulatedMotorLeft.close();
            regulatedMotorLeft = null;
            regulatedMotorRight.close();
            regulatedMotorRight = null;
            regulatedMotorCenter.close();
            regulatedMotorCenter = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSpeed(int speed) {
        Log.d(TAG, "setSpeed ");
        int speedToLeft = (int) (speed * speedFactorLeft);
        int speedToRight = (int) (speed * speedFactorRight);

        commandExecutor.run(new SetSpeedCommand(regulatedMotorLeft, regulatedMotorRight, speedToLeft, speedToRight));
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
        commandExecutor.run(new GoLeftForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed / 2) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }

    public void moveRightForward(int speed) {
        Log.d(TAG, "moveRightForward ");
        commandExecutor.run(new GoRightForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed / 2) * speedFactorRight)));

    }

    public void moveLeftBackward(int speed) {
        Log.d(TAG, "moveLeftBackward ");
        commandExecutor.run(new GoLeftBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed / 2) * speedFactorRight)));

    }

    public void moveRightBackward(int speed) {
        Log.d(TAG, "moveRightBackward ");
        commandExecutor.run(new GoRightBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed / 2) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }

    public void shoot() {
        Log.d(TAG, "shoot()");
        commandExecutor.run(new ShootCommand(regulatedMotorCenter));
    }
}
