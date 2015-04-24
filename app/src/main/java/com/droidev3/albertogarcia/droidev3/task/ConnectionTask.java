package com.droidev3.albertogarcia.droidev3.task;

import android.os.AsyncTask;

import com.droidev3.albertogarcia.droidev3.model.DroidEv3;

import java.io.IOException;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public abstract class ConnectionTask extends AsyncTask<String, Void, DroidEv3> {
    private IOException e;

    /**
     * En hilo diferente a UI
     *
     * @param params
     * @return
     */
    @Override
    protected DroidEv3 doInBackground(String... params) {
        try {
            String ip = params[0];
            return new DroidEv3(ip);
        } catch (IOException e) {
            e.printStackTrace();
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
        super.onPostExecute(droidEv3);
        if (isCancelled()) {
            return;
        }

        if (droidEv3 != null) {
            onConnectedToDroid(droidEv3);
        } else {
            if (e == null) {
                onConnectionError();
            } else {
                onConnectionException(e);
            }
        }
    }

    public abstract void onConnectedToDroid(DroidEv3 droidEv3);

    public abstract void onConnectionException(Exception e);

    public abstract void onConnectionError();
}
