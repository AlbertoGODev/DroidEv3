package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.widget.JoystickView;


public class AnalogJoystickFragment extends BaseFragment {

    private static final String TAG = AnalogJoystickFragment.class.getSimpleName();

    private DroidEv3 droidEv3;
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private JoystickView joystick;

    private int lastDirection = 0;
    private int lastPower = 0;


    private PagerEnabled mDelegate;

    public static AnalogJoystickFragment newInstance() {
        AnalogJoystickFragment analogJoystickFragment = new AnalogJoystickFragment();

        return analogJoystickFragment;
    }

    public void setDelegate(PagerEnabled fragment) {
        Log.e(TAG, "setDelegate()");
        mDelegate = fragment;
    }

    public AnalogJoystickFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        droidEv3 = getDroidEv3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analog_joystick, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setListeners();
    }

    private void bindView(View view) {
        angleTextView = (TextView) view.findViewById(R.id.angleTextView);
        powerTextView = (TextView) view.findViewById(R.id.powerTextView);
        directionTextView = (TextView) view.findViewById(R.id.directionTextView);
        joystick = (JoystickView) view.findViewById(R.id.joystickView);
    }


    private void setListeners() {
        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                Log.d(TAG, "onValueChanged direction = " + direction + " power=" + power);
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");

                Log.d(TAG, "lastDirecion = " + lastDirection + " lastPower=" + lastPower);
                if (checkMoves(power, direction)) {
                    Log.e(TAG, "lastDirection and lastPower changed");
                    lastDirection = direction;
                    lastPower = power;

                    if (droidEv3 != null) {
                        switch (direction) {
                            case JoystickView.FRONT:
                                directionTextView.setText(R.string.front_lab);
                                droidEv3.moveForward(power);
                                break;
                            case JoystickView.FRONT_RIGHT:
                                directionTextView.setText(R.string.front_right_lab);
                                droidEv3.moveRightForward(power);
                                break;
                            case JoystickView.RIGHT:
                                directionTextView.setText(R.string.right_lab);
                                droidEv3.moveRight(power);
                                break;
                            case JoystickView.RIGHT_BOTTOM:
                                directionTextView.setText(R.string.right_bottom_lab);
                                droidEv3.moveRightBackward(power);
                                break;
                            case JoystickView.BOTTOM:
                                directionTextView.setText(R.string.bottom_lab);
                                droidEv3.moveBackward(power);
                                break;
                            case JoystickView.BOTTOM_LEFT:
                                directionTextView.setText(R.string.bottom_left_lab);
                                droidEv3.moveLeftBackward(power);
                                break;
                            case JoystickView.LEFT:
                                directionTextView.setText(R.string.left_lab);
                                droidEv3.moveLeft(power);
                                break;
                            case JoystickView.LEFT_FRONT:
                                directionTextView.setText(R.string.left_front_lab);
                                droidEv3.moveLeftForward(power);
                                break;
                            case 0:
                                if (power > 0) {
                                    directionTextView.setText("perfect_front_lab");
                                    droidEv3.moveForward(power);
                                } else {
                                    droidEv3.stop();
                                }
                                break;
                            default:
                                directionTextView.setText(R.string.center_lab);
                                droidEv3.stop();
                        }
                    }
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        joystick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.e(TAG, "onTouch: " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.e(TAG, "ACTION_DOWN");
                        mDelegate.disable();
                        break;
                    case MotionEvent.ACTION_UP:
                        //Log.e(TAG, "ACTION_UP");
                        mDelegate.enable();
                        break;
                }
                return false;
            }
        });
    }


    public interface PagerEnabled {
        void enable();

        void disable();
    }

    public Boolean checkMoves(int power, int direction) {

        if (direction != lastDirection) {
            Log.e(TAG, "direction != lastDirection");
            return true;
        }
        if (direction == lastDirection && power != lastPower) {
            Log.e(TAG, "direction == lastDirection && power != lastPower");
            //if (power < lastPower || power > lastPower )
            return true;
        }

        return false;
    }
}

