package com.guerritastudio.albertogarcia.droidev3.command;

import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;

/**
 * Created by AlbertoGarcia on 7/5/15.
 */
public class CreateSampleProviderCommand implements Command {

    private DroidEv3 droidEv3;

    public CreateSampleProviderCommand(DroidEv3 droidEv3) {
        this.droidEv3 = droidEv3;
    }

    @Override
    public void run() {
        droidEv3.createSensors();
    }
}
