package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;


public class DigitalJoystickFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = DigitalJoystickFragment.class.getSimpleName();

    private Button stopBtn;
    private Button forwardBtn;
    private Button backwardBtn;
    private Button leftBtn;
    private Button rightBtn;
    private Button setVelBtn;
    private EditText setSpeedET;

    private DroidEv3 droidEv3;
    private int speed = DroidEv3.DEFAULT_SPEED;//default speed motors


    public static DigitalJoystickFragment newInstance() {
        DigitalJoystickFragment fragment = new DigitalJoystickFragment();
        return fragment;
    }

    public DigitalJoystickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        droidEv3 = getDroidEv3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digital_joystick, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setListeners();
    }

    private void bindView(View view) {
        stopBtn = (Button) view.findViewById(R.id.fragment_joystick_stop_btn);
        forwardBtn = (Button) view.findViewById(R.id.fragment_joystick_forward_btn);
        backwardBtn = (Button) view.findViewById(R.id.fragment_joystick_backward_btn);
        leftBtn = (Button) view.findViewById(R.id.fragment_joystick_left_btn);
        rightBtn = (Button) view.findViewById(R.id.fragment_joystick_right_btn);
        setVelBtn = (Button) view.findViewById(R.id.fragment_joystick_setvelocity);
        setSpeedET = (EditText) view.findViewById(R.id.fragment_joystick_et);

    }

    private void setListeners() {
        stopBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        backwardBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        setVelBtn.setOnClickListener(this);
        setSpeedET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setSpeed();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        if (droidEv3 != null) {
            //droidEv3.playBeep(1);
            switch (v.getId()) {
                case R.id.fragment_joystick_stop_btn:
                    Log.d(TAG, "onClick stop");
                    droidEv3.stop();
                    return;
                case R.id.fragment_joystick_forward_btn:
                    Log.d(TAG, "onClick moveForward");
                    droidEv3.moveForward(speed);
                    return;
                case R.id.fragment_joystick_backward_btn:
                    Log.d(TAG, "onClick moveBackward");
                    droidEv3.moveBackward(speed);
                    return;
                case R.id.fragment_joystick_left_btn:
                    Log.d(TAG, "onClick moveLeft");
                    droidEv3.moveLeft(speed);
                    return;
                case R.id.fragment_joystick_right_btn:
                    Log.d(TAG, "onClick moveRight");
                    droidEv3.moveRight(speed);
                    return;
                case R.id.fragment_joystick_setvelocity:
                    Log.d(TAG, "onClick setVelocity");
                    setSpeed();
            }
        }
    }

    private void setSpeed() {
        Log.e(TAG, "setSpeedET is empty = " + setSpeedET.getText().toString().isEmpty());
        if (!setSpeedET.getText().toString().isEmpty()) {
            int speedET = Integer.parseInt(setSpeedET.getText().toString());
            Log.e(TAG, "Speed  = " + speedET);
            if (speedET < 0 || speedET > 100) {
                setSpeedET.setError(getResources().getString(R.string.set_speed_error));
                setSpeedET.requestFocus();
            } else {
                this.speed = speedET;
            }
        } else {
            setSpeedET.setError(getResources().getString(R.string.set_speed_error));
            setSpeedET.requestFocus();
        }
    }
}
