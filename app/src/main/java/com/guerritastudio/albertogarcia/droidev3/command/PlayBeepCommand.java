package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.controller.DroidEv3;

/**
 * Created by AlbertoGarcia on 13/5/15.
 */
public class PlayBeepCommand implements Command {

    private static final String TAG = PlayBeepCommand.class.getSimpleName();

    private DroidEv3 droidEv3;
    private int aCode;

    public PlayBeepCommand(DroidEv3 droidEv3, int aCode) {
        this.droidEv3 = droidEv3;
        this.aCode = aCode;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");
        //Play beep sound:
        if (aCode >= 0 || aCode <= 4) {
            playBeep(aCode);
        } else {
            Log.e(TAG, "aCode must be between 0 and 4");
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
