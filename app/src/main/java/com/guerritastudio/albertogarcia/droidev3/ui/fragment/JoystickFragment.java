package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseActionBarActivity;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.app.MyApp;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;


public class JoystickFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = JoystickFragment.class.getSimpleName();

    private Button stopBtn;
    private Button forwardBtn;
    private Button backwardBtn;
    private Button leftBtn;
    private Button rightBtn;
    private Button setVelBtn;
    private EditText setVelET;

    private DroidEv3 droidEv3;


    public static JoystickFragment newInstance(int sectionNumber) {
        JoystickFragment fragment = new JoystickFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DrawerActivity.KEY_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public JoystickFragment() {
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
        droidEv3 = getDroidEv3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_joystick, container, false);
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
        setVelET = (EditText) view.findViewById(R.id.fragment_joystick_et);

    }

    private void setListeners() {
        stopBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        backwardBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        setVelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick with droidEv3: " + droidEv3.getBrickInfo().getIPAddress());
        //droidEv3.playBeep(1);
        switch (v.getId()) {
            case R.id.fragment_joystick_stop_btn:
                Log.d(TAG, "onClick stop");
                droidEv3.stop();
                return;
            case R.id.fragment_joystick_forward_btn:
                Log.d(TAG, "onClick moveForward");
                droidEv3.moveForward(DroidEv3.MOTORS_SPEED);
                return;
            case R.id.fragment_joystick_backward_btn:
                Log.d(TAG, "onClick moveBackward");
                droidEv3.moveBackward(DroidEv3.MOTORS_SPEED);
                return;
            case R.id.fragment_joystick_left_btn:
                Log.d(TAG, "onClick moveLeft");
                droidEv3.moveLeft(DroidEv3.MOTORS_SPEED);
                return;
            case R.id.fragment_joystick_right_btn:
                Log.d(TAG, "onClick moveRight");
                droidEv3.moveRight(DroidEv3.MOTORS_SPEED);
                return;
            case R.id.fragment_joystick_setvelocity:
                Log.d(TAG,"onClick setVelocity");
                droidEv3.setSpeed(Integer.parseInt(setVelET.getText().toString()));
        }
    }
}
