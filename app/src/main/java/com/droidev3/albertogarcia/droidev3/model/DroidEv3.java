package com.droidev3.albertogarcia.droidev3.model;


import java.io.IOException;

import lejos.hardware.Audio;
import lejos.hardware.lcd.TextLCD;
import lejos.remote.ev3.RemoteRequestEV3;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class DroidEv3 extends RemoteRequestEV3{

    private String ipAddress;
    private Audio audio;
    private TextLCD textLCD;


    public DroidEv3(String ipAddress) throws IOException {
        super(ipAddress);
        this.ipAddress = ipAddress;
        this.audio = getAudio();
    }

    public void playBeep() {
        audio.systemSound(3);

    }
}