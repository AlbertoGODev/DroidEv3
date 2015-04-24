package com.droidev3.albertogarcia.droidev3.app;

import android.support.v7.app.ActionBarActivity;

import com.droidev3.albertogarcia.droidev3.model.DroidEv3;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class BaseActionBarActivity extends ActionBarActivity {


    protected void setDroidEv3(DroidEv3 droidEv3) {
        MyApp myApp = (MyApp) getApplication();
        myApp.setDroidEv3(droidEv3);
    }

    protected DroidEv3 getDroidEv3() {
        MyApp myApp = (MyApp) getApplication();
        return myApp.getDroidEv3();
    }

    protected void disconnectDroidEv3() {
        MyApp myApp = (MyApp) getApplication();
        DroidEv3 droidEv3 = myApp.getDroidEv3();
        if (droidEv3 != null) {
            droidEv3.disConnect();
        }
    }

}
