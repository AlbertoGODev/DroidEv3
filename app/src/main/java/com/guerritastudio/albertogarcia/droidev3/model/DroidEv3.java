package com.guerritastudio.albertogarcia.droidev3.model;


import android.util.Log;

import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.lcd.TextLCD;
import lejos.remote.ev3.RMIMenu;
import lejos.remote.ev3.RemoteRequestEV3;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class DroidEv3 extends RemoteRequestEV3{

    private static final String TAG = DroidEv3.class.getSimpleName();

    private RMIMenu menu;

    private String ipAddress;
    private Audio audio;
    private TextLCD textLCD;


    public DroidEv3(String ipAddress) throws IOException {
        super(ipAddress);
        Log.d(TAG, "DroidEv3() with ipAddress:" + ipAddress);
        this.ipAddress = ipAddress;
        this.audio = getAudio();
    }

    public void playBeep() {
        audio.systemSound(3);

    }
}