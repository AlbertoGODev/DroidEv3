package com.guerritastudio.albertogarcia.droidev3.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class BaseFragment extends Fragment {


    protected void setDroidEv3(DroidEv3 droidEv3) {
        MyApp myApp = (MyApp) getActivity().getApplication();
        myApp.setDroidEv3(droidEv3);
    }

    protected DroidEv3 getDroidEv3() {
        MyApp myApp = (MyApp) getActivity().getApplication();
        return myApp.getDroidEv3();
    }

    protected void disconnectDroidEv3() {
        MyApp myApp = (MyApp) getActivity().getApplication();
        DroidEv3 droidEv3 = myApp.getDroidEv3();
        if (droidEv3 != null) {
            droidEv3.disConnect();
        }
    }

}