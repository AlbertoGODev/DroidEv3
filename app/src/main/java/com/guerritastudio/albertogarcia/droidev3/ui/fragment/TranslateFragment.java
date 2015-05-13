package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.task.ConnectionTask;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import lejos.remote.ev3.RemoteRequestMenu;


public class TranslateFragment extends BaseFragment implements TextToSpeech.OnInitListener {

    private static final String TAG = TranslateFragment.class.getSimpleName();
    private TextView menuInfoTV;
    private Button toSpeechBTN;
    private EditText toSpeechET;
    private TextToSpeech textToSpeech;
    private RemoteRequestMenu menu;

    public static TranslateFragment newInstance(int sectionNumber) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DrawerActivity.KEY_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TranslateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DrawerActivity) activity).onSectionAttached(
                getArguments().getInt(DrawerActivity.KEY_SECTION_NUMBER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(getActivity(), this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.getDefault());
        } else if (status == TextToSpeech.ERROR) {
            Log.e(TAG, "Error occurred while initializing Text-To-Speech engine");
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setListener();
    }

    private void bindView(View view) {
        menuInfoTV = (TextView) view.findViewById(R.id.fragment_translate_menu_info_tv);
        toSpeechBTN = (Button) view.findViewById(R.id.to_speech_btn);
        toSpeechET = (EditText) view.findViewById(R.id.to_speech_et);
    }

    private void setListener() {
        toSpeechBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText(toSpeechET.getText().toString());
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.e(TAG, "onStart()");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.e(TAG, "onDone");
                /*File[] files = getActivity().getCacheDir().listFiles();
                Log.d(TAG, "files length = " + files.length);
                for (int i = 0; i < files.length; i++) {
                    Log.d(TAG, "file = " + files[i].getName() + " , bytes  = " + files[i].length());
                }*/
                new CreateMenuTask().execute(getDroidEv3().getBrickInfo().getIPAddress());
            }

            @Override
            public void onError(String utteranceId) {
                Log.e("TAG", "onError()");
            }
        });
    }

    private void sendText(String text) {

        Log.d(TAG, "sendText()");

        //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        HashMap<String, String> myHashRender = new HashMap<>();
        String destFileName = getActivity().getCacheDir().getAbsolutePath() + "/prueba.wav";
        Log.e(TAG, "destFileName = " + destFileName);
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        int success = textToSpeech.synthesizeToFile(text, myHashRender, destFileName);
        if (success == TextToSpeech.ERROR) {
            Log.e(TAG, "error");
        } else {
            Log.e(TAG, "success true");
        }
    }


    private class CreateMenuTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // Create the remote menu
            try {
                Log.d(TAG, "CreateMenuTask doInBackground params = " + params[0]);
                if (menu == null) {
                    Log.e(TAG, "menu is null");
                    disconnectDroidEv3();//Para poder obtener el menu tengo que desconectarme del robot...
                    menu = new RemoteRequestMenu(params[0]);
                }else{
                    menu.resume();
                }
                uploadFile(new File(getActivity().getCacheDir(), "prueba.wav"));
                menu = null;
                Log.e(TAG,"menu == null");
                setDroidEv3(new DroidEv3(params[0]));//se queda bloqueado............
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            //Log.e(TAG, "menu version= " + menu.getMenuVersion());
            return null;
        }
    }

    /**
     * Upload the specified file
     */
    private void uploadFile(File file) {
        Log.d(TAG, "uploadFile()");
        if (menu == null) {
            return;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            Log.d(TAG, " Uploading " + file.getName());
            menu.uploadFile("/home/lejos/programs/" + file.getName(), data);
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e(TAG, " IOException uploading file");
        }
    }
}
