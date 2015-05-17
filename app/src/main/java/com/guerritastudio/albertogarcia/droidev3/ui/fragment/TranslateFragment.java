package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.app.ConstDroidEv3;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class TranslateFragment extends BaseFragment implements TextToSpeech.OnInitListener {

    private static final String TAG = TranslateFragment.class.getSimpleName();
    private TextView menuInfoTV;
    private ImageButton toSpeechBTN;
    private EditText toSpeechET;
    private TextToSpeech textToSpeech;

    public static TranslateFragment newInstance(int sectionNumber) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstDroidEv3.KEY_SECTION_NUMBER, sectionNumber);
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
                getArguments().getInt(ConstDroidEv3.KEY_SECTION_NUMBER));
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
        toSpeechBTN = (ImageButton) view.findViewById(R.id.to_speech_btn);
        toSpeechET = (EditText) view.findViewById(R.id.to_speech_et);
    }

    private void setListener() {
        toSpeechBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeechET.length() > 0) {
                    sendText(toSpeechET.getText().toString());
                }
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
                if (getDroidEv3() != null) {
                    uploadFile();
                }
            }

            @Override
            public void onError(String utteranceId) {
                Log.e("TAG", "onError()");
            }
        });

        toSpeechET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //Log.d(TAG, "onKey() = KEYCODE_ENTER");
                    if (toSpeechET.length() > 0) {
                        sendText(toSpeechET.getText().toString());
                    }
                }
                return true;
            }
        });
    }

    private void sendText(String text) {
        Log.d(TAG, "sendText()");

        if (getDroidEv3() == null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);//To play speech in mobile.
        } else {
            HashMap<String, String> myHashRender = new HashMap<>();
            String destFileName = getActivity().getCacheDir().getAbsolutePath() + "/" + ConstDroidEv3.AUDIO_FILE_NAME;
            Log.e(TAG, "destFileName = " + destFileName);
            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
            textToSpeech.synthesizeToFile(text, myHashRender, destFileName);
        }
        toSpeechET.setText("");
    }

    private void uploadFile() {
        try {
            Log.d(TAG, "UploadFile");
            File file = new File(getActivity().getCacheDir(), ConstDroidEv3.AUDIO_FILE_NAME);
            getDroidEv3().uploadFile(file);
            Log.e(TAG, "uploadFile finished");
            //Poner en el upload o mirar si espera que termine el upload.....
            getDroidEv3().playSpeech(file);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
