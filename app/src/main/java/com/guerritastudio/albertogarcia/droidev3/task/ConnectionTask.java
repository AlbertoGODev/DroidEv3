package com.guerritastudio.albertogarcia.droidev3.task;

import android.os.AsyncTask;
import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.controller.DroidEv3;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public abstract class ConnectionTask extends AsyncTask<String, Void, DroidEv3> {
    private static final String TAG = ConnectionTask.class.getSimpleName();

    private Exception e;

    /**
     * En hilo diferente a UI
     *
     * @param params
     * @return
     */
    @Override
    protected DroidEv3 doInBackground(String... params) {
        Log.d(TAG, "doInBackground with params:" + params[0]);
        try {
            String ip = params[0];

            BrickInfo[] devices = BrickFinder.discover();//If Ev3 or mobile is disconnected discover() will throw SocketTimeoutException.

            Log.e(TAG, "Number of devices discover:" + devices.length);

            for (int i = 0; i < devices.length; i++) {
                BrickInfo brickInfo = devices[i];
                if (brickInfo.getIPAddress().equals(ip)) {
                    Log.e(TAG, "BrickFinder has been discover!!!");
                    return new DroidEv3(brickInfo);
                }
            }

        } catch (Exception e) {
            this.e = e;
            e.printStackTrace();
            Log.e(TAG, "doInBackground() exception == " + e);
        }
        return null;
    }

    /**
     * En hilo de UI
     *
     * @param droidEv3
     */
    @Override
    protected void onPostExecute(DroidEv3 droidEv3) {
        Log.d(TAG, "onPostExecute() droidEv3: " + droidEv3);
        if (isCancelled()) {
            return;
        }

        if (droidEv3 != null) {
            onConnectedToDroid(droidEv3);
        } else {
            if (e == null) {
                Log.e(TAG, "onPostExecute() exception == null");
                onConnectionError();
            } else {
                Log.e(TAG, "onPostExecute() exception not null");
                onConnectionException(e);
            }
        }

        super.onPostExecute(droidEv3);
    }

    public abstract void onConnectedToDroid(DroidEv3 droidEv3);

    public abstract void onConnectionException(Exception e);

    public abstract void onConnectionError();
}
