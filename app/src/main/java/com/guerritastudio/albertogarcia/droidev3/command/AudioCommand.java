package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import lejos.hardware.Audio;
import lejos.robotics.RegulatedMotor;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */
public class AudioCommand implements Command {

    private static final String TAG = AudioCommand.class.getSimpleName();

    private Audio audio;

    public AudioCommand(Audio audio){
        this.audio = audio;
    }

    @Override
    public void run() {
        Log.e(TAG,"run()");
       audio.playTone(500,500);
    }
}
