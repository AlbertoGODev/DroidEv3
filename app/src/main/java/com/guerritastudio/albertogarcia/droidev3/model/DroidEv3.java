package com.guerritastudio.albertogarcia.droidev3.model;


import android.os.AsyncTask;
import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.command.*;

import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.BrickInfo;
import lejos.hardware.LED;
import lejos.hardware.Power;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.remote.ev3.RemoteRequestMenu;
import lejos.remote.ev3.RemoteRequestSampleProvider;
import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class DroidEv3 extends RemoteRequestEV3 {

    private static final String TAG = DroidEv3.class.getSimpleName();

    //Para pruebas:
    public static final int DEFAULT_SPEED = 10;
    private final int MOTOR_LEFT_SPEED = 10;
    private final int MOTOR_RIGHT_SPEED = 10;
    private float speedFactorLeft;
    private float speedFactorRight;

    private BrickInfo brickInfo;
    private Audio audio;
    private LED led;
    public static int lastLedPattern = 0;
    private Power power;

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

    public DroidEv3(String ip) throws IOException{
        super(ip);
        Log.d(TAG, "DroidEv3(ip) constructor");
    }

    private void initializeComponents() {
        /*power = getPower();
        led = getLED();
        initMotors();
        setMotorsSpeed();
        commandExecutor = CommandExecutor.getInstance();*/
        //createSensors();

        //new CreateMenuTask().execute(brickInfo.getIPAddress());

        Log.d(TAG, "Components started correctly, ipAddress:" + brickInfo.getIPAddress() + " nameBrick: " + brickInfo.getName());
    }

    //BrickInfo:
    public BrickInfo getBrickInfo() {
        return brickInfo;
    }

    //Power Info:
    public void fetchPowerInfo(GetPowerInfoCommand.OnPowerInfo callback) {
        //Log.d(TAG, "fetchPowerInfo()");
        commandExecutor.run(new GetPowerInfoCommand(power, callback, touchSensorSP, irSensorSP, colorSP));
    }

    //Led Control:
    public void setLEDPattern(int pattern) {
        //Log.d(TAG, "setLEDPattern with pattern = " + pattern + " and lastLedPattern = " + lastLedPattern);
        if (pattern != lastLedPattern) {
            commandExecutor.run(new SetLedPatternCommand(led, pattern));
        }
    }


    //Motors:

    private void initMotors() {
        //L (for large), M (for medium) or G (for glide wheel)
        regulatedMotorLeft = createRegulatedMotor("B", 'L');
        regulatedMotorRight = createRegulatedMotor("C", 'L');
        regulatedMotorCenter = createRegulatedMotor("A", 'L');
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

    //Sensors:

    public void createSensors() {
        Log.d(TAG, "createSensors()");
        touchSensorSP = (RemoteRequestSampleProvider) createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", "Touch");
        Log.e(TAG, "touchSensorSP created");
        irSensorSP = (RemoteRequestSampleProvider) createSampleProvider("S4", "lejos.hardware.sensor.EV3IRSensor", "Distance");
        Log.e(TAG, "irSensorSP created");
        colorSP = (RemoteRequestSampleProvider) createSampleProvider("S3", "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
        Log.e(TAG, "colorSP created");
    }

    public void closeSensors() {
        Log.d(TAG, "closeSensors()");
        if (touchSensorSP != null) {
            touchSensorSP.close();
        }
        if (irSensorSP != null) {
            irSensorSP.close();
        }
        if (colorSP != null) {
            colorSP.close();
        }
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
        commandExecutor.run(new GoLeftForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed / 3) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }

    public void moveRightForward(int speed) {
        Log.d(TAG, "moveRightForward ");
        commandExecutor.run(new GoRightForwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed / 3) * speedFactorRight)));

    }

    public void moveLeftBackward(int speed) {
        Log.d(TAG, "moveLeftBackward ");
        commandExecutor.run(new GoLeftBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) (speed * speedFactorLeft), (int) ((speed / 3) * speedFactorRight)));

    }

    public void moveRightBackward(int speed) {
        Log.d(TAG, "moveRightBackward ");
        commandExecutor.run(new GoRightBackwardCommand(regulatedMotorLeft, regulatedMotorRight, (int) ((speed / 3) * speedFactorLeft), (int) (speed * speedFactorRight)));

    }

    public void shoot() {
        Log.d(TAG, "shoot()");
        commandExecutor.run(new ShootCommand(regulatedMotorCenter));
    }


    //Sounds:
/*
    //Prueba de sonido....luego poner en comando.
    public void playNoteScale() {
        Log.e(TAG, "playNoteScale()");
        commandExecutor.run(new AudioCommand(audio));
        getAudio().playTone(500, 1000);
        Log.e(TAG, "playNoteScale() volumen= " + audio.getVolume());
        //audio.systemSound(1);
       *//* for (int freq = 2500; freq > 1000; freq -= 100) {
            audio.playNote(Sound.FLUTE, freq, 100);
        }*//*
    }

    //Play beep sound:
    *//*
        aCode	Resulting Sound
        0	short beep
        1	double beep
        2	descending arpeggio
        3	ascending arpeggio
        4	long, low buzz
        *//*
    public void playBeep(int aCode) {
        audio.systemSound(aCode);
    }*/

  /*  private class CreateMenuTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Create the remote menu
            try {
                Log.d(TAG, "CreateMenuTask doInBackground params = "+params[0]);
                if (menu == null) {
                    menu = new RemoteRequestMenu(params[0]);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            Log.e(TAG, "menu version= " + menu.getMenuVersion());
            return null;

        }
    }*/
}
