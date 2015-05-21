package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.app.ConstDroidEv3;
import com.guerritastudio.albertogarcia.droidev3.controller.DroidEv3;

import java.io.File;

/**
 * Created by AlbertoGarcia on 13/5/15.
 */
public class ToSpeechCommand implements Command {

    private static final String TAG = ToSpeechCommand.class.getSimpleName();

    private DroidEv3 droidEv3;
    private File file;

    public ToSpeechCommand(DroidEv3 droidEv3, File file) {
        this.droidEv3 = droidEv3;
        this.file = file;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");
        try {
            Log.d(TAG, " Playing file = " + ConstDroidEv3.AUDIO_DIRECTORY + file.getName());
            droidEv3.getAudio().playSample(new File(ConstDroidEv3.AUDIO_DIRECTORY + file.getName()));
        } catch (Exception ioe) {
            Log.e(TAG, "IO Exception playing sound file");
            ioe.printStackTrace();
        }
    }
}
