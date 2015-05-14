package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteRequestMenu;


public class TranslateFragment extends BaseFragment implements TextToSpeech.OnInitListener {

    private static final String TAG = TranslateFragment.class.getSimpleName();
    private static final String FILE_NAME = "pruebas.wav";
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
                /*File[] files = getActivity().getCacheDir().listFiles();
                Log.d(TAG, "files length = " + files.length);
                for (int i = 0; i < files.length; i++) {
                    Log.d(TAG, "file = " + files[i].getName() + " , bytes  = " + files[i].length());
                }*/

                Log.e(TAG, "onDone");
                uploadFile();

            }

            @Override
            public void onError(String utteranceId) {
                Log.e("TAG", "onError()");
            }
        });
    }

    private void sendText(String text) {
        Log.d(TAG, "sendText()");
        //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);//To play speech in mobile.
        HashMap<String, String> myHashRender = new HashMap<>();
        String destFileName = getActivity().getCacheDir().getAbsolutePath() + "/" + FILE_NAME;
        Log.e(TAG, "destFileName = " + destFileName);
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        textToSpeech.synthesizeToFile(text, myHashRender, destFileName);
        //int success = textToSpeech.synthesizeToFile(text, myHashRender, destFileName);
/*        if (success == TextToSpeech.ERROR) {
            Log.e(TAG, "error");
        } else {
            Log.e(TAG, "success true");
        }*/
    }

    private void uploadFile() {
        try {
            Log.d(TAG, "UploadFile");
            File file = new File(getActivity().getCacheDir(), FILE_NAME);
            getDroidEv3().uploadFile(file);

            //Poner en el upload o mirar si espera que termine el upload.....
            //getDroidEv3().playSpeech(file);//no funciona..

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
