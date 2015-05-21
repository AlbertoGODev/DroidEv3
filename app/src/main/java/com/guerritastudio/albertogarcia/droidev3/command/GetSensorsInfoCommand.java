package com.guerritastudio.albertogarcia.droidev3.command;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import lejos.hardware.Power;
import lejos.remote.ev3.RemoteRequestSampleProvider;

/**
 * Created by AlbertoGarcia on 5/5/15.
 */
public class GetSensorsInfoCommand implements Command {

    private static final String TAG = GetSensorsInfoCommand.class.getSimpleName();

    private OnPowerInfo onPowerInfo;

    private Power power;
    private RemoteRequestSampleProvider touchSensorSP;
    private RemoteRequestSampleProvider irSensorSP;
    private RemoteRequestSampleProvider colorSP;


    public GetSensorsInfoCommand(Power power, OnPowerInfo onPowerInfo, RemoteRequestSampleProvider touchSensorSP, RemoteRequestSampleProvider irSensorSP, RemoteRequestSampleProvider colorSP) {
        this.power = power;
        this.onPowerInfo = onPowerInfo;
        this.touchSensorSP = touchSensorSP;
        this.irSensorSP = irSensorSP;
        this.colorSP = colorSP;
    }

    @Override
    public void run() {
        try {
            Log.e(TAG, "run()");
            int voltage = power.getVoltageMilliVolt();
            float currentDraw = power.getBatteryCurrent();
            float[] samples = new float[3];

            if (touchSensorSP != null) {
                touchSensorSP.fetchSample(samples, 0);
                //Log.d(TAG, "fetchSample touch");
            } else {
                Log.e(TAG, "touchSensorSP == null");
            }
            if (irSensorSP != null) {
                irSensorSP.fetchSample(samples, 1);
                //Log.d(TAG, "fetchSample ir");
            } else {
                Log.e(TAG, "irSensorSP == null");
            }
            if (colorSP != null) {
                colorSP.fetchSample(samples, 2);
                //Log.d(TAG, "fetchSample color");
            } else {
                Log.e(TAG, "colorSP == null");
                samples[2] = -1;
            }

            publish(voltage, currentDraw, (int) samples[0], (int) samples[1], (int) samples[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publish(final int voltage, final float currentDraw, final int touchSample, final int irSample, final int colorSample) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPowerInfo.currentPowerInfo(voltage, currentDraw, touchSample, irSample, colorSample);
            }
        });
    }

    public interface OnPowerInfo {
        public void currentPowerInfo(int voltageMV, float currentDrawA, int touchSample, int irSample, int colorSample);
    }
}
