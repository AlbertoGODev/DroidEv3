/*package com.droidev3.albertogarcia.droidev3.ui.activity;

*//**
 * Created by AlbertoGarcia on 23/4/15.
 */
/*    import java.io.IOException;
    import lejos.hardware.Audio;
    import lejos.remote.ev3.RemoteRequestEV3;
    import lejos.robotics.RegulatedMotor;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.app.Activity;
    import android.view.Menu;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.widget.Button;
    import android.widget.Toast;

    public class Notas extends Activity implements OnClickListener {

        private RemoteRequestEV3 ev3;
        private RegulatedMotor left, right;
        private Button connect;
        private Audio audio;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button left = (Button) findViewById(R.id.left);
            Button right = (Button) findViewById(R.id.right);
            Button stop = (Button) findViewById(R.id.stop);
            Button forward = (Button) findViewById(R.id.forward);
            Button backward = (Button) findViewById(R.id.backward);
            connect = (Button) findViewById(R.id.connect);
            left.setOnClickListener(this);
            right.setOnClickListener(this);
            stop.setOnClickListener(this);
            forward.setOnClickListener(this);
            backward.setOnClickListener(this);
            connect.setOnClickListener(this);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.left) new Control().execute("rotate left");
            else if (v.getId() == R.id.right) new Control().execute("rotate right");
            else if (v.getId() == R.id.stop) new Control().execute("stop");
            else if (v.getId() == R.id.forward) new Control().execute("forward");
            else if (v.getId() == R.id.backward) new Control().execute("backward");
            else if (v.getId() == R.id.connect) {
                if (ev3 == null) {
                    new Control().execute("connect","192.168.0.9");
                    connect.setText("Disconnect");
                }
                else {
                    new Control().execute("disconnect");
                    connect.setText("Connect");
                }
            }
        }

        private class Control extends AsyncTask<String, Integer, Long> {

            protected Long doInBackground(String... cmd) {

                if (cmd[0].equals("connect")) {
                    try {
                        ev3 = new RemoteRequestEV3(cmd[1]);
                        left = ev3.createRegulatedMotor("A", 'L');
                        right = ev3.createRegulatedMotor("B", 'L');
                        audio = ev3.getAudio();
                        audio.systemSound(3);
                        return 0l;
                    } catch (IOException e) {
                        return 1l;
                    }
                } else if (cmd[0].equals("disconnect") && ev3 != null) {
                    audio.systemSound(2);
                    left.close();
                    right.close();
                    ev3.disConnect();
                    ev3 = null;
                    return 0l;
                }

                if (ev3 == null) return 2l;

                ev3.getAudio().systemSound(1);

                if (cmd[0].equals("stop")) {
                    left.stop(true);
                    right.stop(true);
                } else if (cmd[0].equals("forward")) {
                    left.forward();
                    right.forward();
                } else if (cmd[0].equals("backward")) {
                    left.backward();
                    right.backward();
                } else if (cmd[0].equals("rotate left")) {
                    left.backward();
                    right.forward();
                } else if (cmd[0].equals("rotate right")) {
                    left.forward();
                    right.backward();
                }

                return 0l;
            }

            protected void onPostExecute(Long result) {
                if (result == 1l) Toast.makeText(Notas.this, "Could not connect to EV3", Toast.LENGTH_LONG).show();
                else if (result == 2l) Toast.makeText(Notas.this, "Not connected", Toast.LENGTH_LONG).show();
            }
        }

    }
}*/



//MariTTS:

/*
package org.fuzzycow.lego.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import lejos.internal.io.NativeDevice;
import lejos.nxt.Battery;
import lejos.nxt.LocalBattery;
import lejos.nxt.Sound;
import lejos.util.Delay;
import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;

public class TTS  implements Runnable  {
    public final static TTS INSTANCE = new TTS();

    private final static NativeDevice dev = new NativeDevice("/dev/lms_sound");
    private static final byte OP_PLAY = 2;
    private static final byte OP_SERVICE = 4;
    private static final int PCM_BUFFER_SIZE = 250;
    public static final int VOL_MAX = 100;

    private int vol = VOL_MAX;

    private MaryInterface marytts = null;

    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public static void main(String[] args) throws Exception {
        Logger.setLevel(Level.ALL);

        Battery battery = new LocalBattery();
        float voltLevel = battery.getVoltage();
        Logger.info("Volatage " + voltLevel);

        TTS tts = TTS.getInstance();
        Thread thread = new Thread(tts);

        // just for this test;
        tts.connect("192.168.1.10", 59125);
        // should be true in real scenario
        thread.setDaemon(false);
        Sound.setVolume(VOL_MAX);
        tts.say("Hello");
        tts.say("I am a robot");
        tts.say("yes, yes, a robot");
        tts.say("1 2 3 4 5 6 7 8 9 11 12 13 14 15");
        tts.say("2013");

        thread.start();

        thread.join();
    }

    public static TTS getInstance() {
        return INSTANCE;
    }

    public void run() {
        while (true) {
            String mesg = null;
            try {
                //while ((mesg = queue.poll(100,TimeUnit.MILLISECONDS)) != null) {
                while (  ( mesg = queue.take() ) != null ) {
                    sayString(mesg);
                }
            } catch (InterruptedException e) {
                Logger.error(e);
            }
        }
    }

    public  synchronized void setVolume(int v) {
        vol = v;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public int say(String text) {
        Logger.debug("TTS: adding to queue " + text);
        queue.add(text);
        return queue.size();
    }



    public synchronized boolean  isConnected() {
        if ( marytts != null )
            return true;
        else
            return false;
    }

    public synchronized void connect(String hostname, int port) {
        Logger.info("TTS Connecting to service");
        try {
            marytts = new RemoteMaryInterface(hostname, port);
            Set<String> voices = marytts.getAvailableVoices();
            Logger.info("TTS available voices: " + voices);
            // marytts.setVoice(voices.iterator().next());getClass();
            //marytts.setVoice("cmu-rms-hsmm");

            // NOTE !!! change the voice name to one you have installed !!!
            marytts.setVoice("dfki-poppy");
            // TractScaler    amount:1.4;
            // Robot amount:40.0;
            // marytts.setAudioEffects("Volume(Amount=3.0), Robot(Amount=40), TractScaler(amount=1.4)");
            //marytts.setAudioEffects("Volume(Amount=2.0), TractScaler(amount=1.4)");
            Logger.info("TTS Available audio effects: " + marytts.getAudioEffects() );

            Logger.info("TTS Connected to service");
        } catch (IOException ioe) {
            Logger.error(ioe);
            marytts = null;
            return;
        }
    }



    public void clear() {
        queue.clear();
    }

    private void sayString(String text) {

        Logger.info("TTS will now say: [" + text + "]");

        AudioInputStream ais = null;
        ;
        try {
            ais = marytts.generateAudio(text);
        } catch (Exception e) {
            Logger.error(e);
            return;
        }

        AudioFormat outDataFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_UNSIGNED, 8000.0F, 8, 1, 1, 8000.0F,
                false);

        AudioInputStream lowResAIS = null;

        if (!AudioSystem.isConversionSupported(outDataFormat, ais.getFormat())) {
            Logger.error("Can't convert audiostream into " + outDataFormat);
            return;
        }

        try {
            lowResAIS = AudioSystem.getAudioInputStream(outDataFormat, ais);
        } catch (IllegalArgumentException iae) {
            Logger.error(iae);
            return;
        }

        playAudioInputStream(lowResAIS, vol);
    }

    private synchronized void playAudioInputStream(AudioInputStream ais, int vol) {
        byte[] buf = new byte[PCM_BUFFER_SIZE * 4 + 1];

        byte[] header = new byte[3];
        // get ready to play, set the volume
        header[0] = OP_PLAY;
        header[1] = (byte) ((vol * 8) / 100);
        header[2] = (byte)'T';

        int dataLen = 0;
        int offset = 0;

        Logger.debug("TTS writing to sound device");
        try {
            dev.write(header,0, 3);

            // buf[1] = 0;

            while ((dataLen = ais.read(buf, 1, buf.length - 1)) > 0) {

                offset = 0;

                while (offset < dataLen) {
                    buf[offset] = OP_SERVICE;
                    int len = dataLen - offset;
                    if (len > PCM_BUFFER_SIZE)
                        len = PCM_BUFFER_SIZE;
                    int written = dev.write(buf, offset, len + 1);

                    if ( written < dataLen) {
                        Delay.msDelay(3);
                    }
                    offset += written;

                }

            }

            Logger.debug("TTS wrote to sound device " + offset);
        } catch (Exception e) {
            Logger.error(e);
            Delay.msDelay(100);
        }

    }


}*/
