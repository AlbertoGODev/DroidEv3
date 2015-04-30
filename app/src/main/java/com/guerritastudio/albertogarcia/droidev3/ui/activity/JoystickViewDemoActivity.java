package com.guerritastudio.albertogarcia.droidev3.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseActionBarActivity;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.zerokol.views.JoystickView;

public class JoystickViewDemoActivity extends BaseActionBarActivity {
    private static final String TAG = JoystickViewDemoActivity.class.getSimpleName();

    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    // Importing also other views
    private JoystickView joystick;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);
        //Referencing also other views
        joystick = (JoystickView) findViewById(R.id.joystickView);

        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                Log.d(TAG, "onValueChanged speed=" + power);
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                // getDroidEv3().setSpeed(power);

             /*   switch (power){
                    case 2:
                        getDroidEv3().setSpeed(2);
                        return;
                    case 5:
                        getDroidEv3().setSpeed(5);
                        return;
                    case 10:
                        getDroidEv3().setSpeed(10);
                        return;
                    case 15:
                        getDroidEv3().setSpeed(15);
                        return;
                    case 20:
                        getDroidEv3().setSpeed(20);
                      return;
                    case 25:
                        getDroidEv3().setSpeed(25);
                        return;
                    case 30:
                        getDroidEv3().setSpeed(30);
                        return;
                    case 35:
                        getDroidEv3().setSpeed(35);
                        return;
                    case 40:
                        getDroidEv3().setSpeed(40);
                        return;
                    case 45:
                        getDroidEv3().setSpeed(45);
                        return;
                    case 50:
                        getDroidEv3().setSpeed(50);
                        return;
                    default:
                        Log.d(TAG,"default");
                }*/
                /*if (power == 50) {
                    Log.d(TAG,"onValueChanged speed is 50");
                    //5 para no hacer ruido...
                    getDroidEv3().setSpeed(5);
                }*/

                switch (direction) {
                    case JoystickView.FRONT:
                        directionTextView.setText(R.string.front_lab);
                        getDroidEv3().moveForward(power);
                        break;
                    case JoystickView.FRONT_RIGHT:
                        directionTextView.setText(R.string.front_right_lab);
                        getDroidEv3().moveRightForward(power);
                        break;
                    case JoystickView.RIGHT:
                        directionTextView.setText(R.string.right_lab);
                        getDroidEv3().moveRight(power);
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        directionTextView.setText(R.string.right_bottom_lab);
                        getDroidEv3().moveRightBackward(power);
                        break;
                    case JoystickView.BOTTOM:
                        directionTextView.setText(R.string.bottom_lab);
                        getDroidEv3().moveBackward(power);
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        directionTextView.setText(R.string.bottom_left_lab);
                        getDroidEv3().moveLeftBackward(power);
                        break;
                    case JoystickView.LEFT:
                        directionTextView.setText(R.string.left_lab);
                        getDroidEv3().moveLeft(power);
                        break;
                    case JoystickView.LEFT_FRONT:
                        directionTextView.setText(R.string.left_front_lab);
                        getDroidEv3().moveLeftForward(power);
                        break;
                    default:
                        directionTextView.setText(R.string.center_lab);
                        getDroidEv3().stop();
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "on Destroy() disconnectDroidEv3()");
        disconnectDroidEv3();
    }
}