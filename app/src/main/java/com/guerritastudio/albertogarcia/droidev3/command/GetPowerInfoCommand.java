package com.guerritastudio.albertogarcia.droidev3.command;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import lejos.hardware.Power;

/**
 * Created by AlbertoGarcia on 5/5/15.
 */
public class GetPowerInfoCommand implements Command {

    private static final String TAG = GetPowerInfoCommand.class.getSimpleName();

    private Power power;
    private OnPowerInfo onPowerInfo;

    public GetPowerInfoCommand(Power power, OnPowerInfo onPowerInfo) {
        this.power = power;
        this.onPowerInfo = onPowerInfo;
    }

    @Override
    public void run() {
        Log.e(TAG, "run()");
        int voltage = power.getVoltageMilliVolt();
        float currentDraw = power.getBatteryCurrent();
        publish(voltage, currentDraw);
    }

    private void publish(final int voltage, final float currentDraw) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPowerInfo.currentPowerInfo(voltage, currentDraw);
            }
        });
    }

    public interface OnPowerInfo {
        public void currentPowerInfo(int voltageMV, float currentDrawA);
    }
}
