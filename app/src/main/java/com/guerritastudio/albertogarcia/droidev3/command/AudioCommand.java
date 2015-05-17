package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.app.ConstDroidEv3;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;

import java.io.File;

/**
 * Created by AlbertoGarcia on 13/5/15.
 */
public class AudioCommand implements Command {

    private static final String TAG = AudioCommand.class.getSimpleName();

    private DroidEv3 droidEv3;
    private File file;

    public AudioCommand(DroidEv3 droidEv3, File file) {
        this.droidEv3 = droidEv3;
        this.file = file;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");
        try {
            //Play beep sound:
            //playBeep(0);
            Log.d(TAG, " Playing file = " + ConstDroidEv3.AUDIO_DIRECTORY + file.getName());
            droidEv3.getAudio().playSample(new File(ConstDroidEv3.AUDIO_DIRECTORY + file.getName()));
        } catch (Exception ioe) {
            Log.e(TAG, "IO Exception playing sound file");
            ioe.printStackTrace();
        }
    }

    /**
     * aCode  Resulting Sound
     * 0	   short beep
     * 1	   double beep
     * 2	   descending arpeggio
     * 3	   ascending arpeggio
     * 4	   long, low buzz
     *
     * @param aCode
     */
    public void playBeep(int aCode) {
        droidEv3.getAudio().systemSound(aCode);
    }

}
