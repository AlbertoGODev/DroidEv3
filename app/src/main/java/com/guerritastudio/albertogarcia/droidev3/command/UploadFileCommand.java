package com.guerritastudio.albertogarcia.droidev3.command;

import android.util.Log;

import com.guerritastudio.albertogarcia.droidev3.app.ConstDroidEv3;
import com.guerritastudio.albertogarcia.droidev3.controller.DroidEv3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lejos.remote.ev3.RemoteRequestMenu;

/**
 * Created by AlbertoGarcia on 13/5/15.
 */
public class UploadFileCommand implements Command {

    private static final String TAG = UploadFileCommand.class.getSimpleName();
    private RemoteRequestMenu menu;
    private String host;
    private File file;
    private DroidEv3 droidEv3;


    public UploadFileCommand(DroidEv3 droidEv3, String host, File file) {
        this.droidEv3 = droidEv3;
        this.host = host;
        this.file = file;
    }

    /**
     * Upload the specified file
     */
    @Override
    public void run() {
        Log.d(TAG, "run() uploadFile()");
        try {
            droidEv3.disConnect();
            Log.e(TAG, "droidEv3 disConnect()");
            menu = new RemoteRequestMenu(host);
            Log.e(TAG, "Menu created");
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            Log.d(TAG, " Uploading " + ConstDroidEv3.AUDIO_DIRECTORY + file.getName());
            menu.uploadFile(ConstDroidEv3.AUDIO_DIRECTORY + file.getName(), data);
            Log.d(TAG, "File upload correctly");
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e(TAG, " IOException uploading file");
        } finally {
            if (menu != null) {
                try {
                    menu.disConnect();
                    Log.e(TAG, "menu disConnected");
                    droidEv3.reConnect();
                    Log.e(TAG, "droid reConnected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
