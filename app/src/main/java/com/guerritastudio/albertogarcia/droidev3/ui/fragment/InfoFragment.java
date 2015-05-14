package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.app.Utils;
import com.guerritastudio.albertogarcia.droidev3.command.GetPowerInfoCommand.OnPowerInfo;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.util.Timer;
import java.util.TimerTask;


public class InfoFragment extends BaseFragment implements OnPowerInfo, View.OnClickListener {

    private static final String TAG = InfoFragment.class.getSimpleName();

    private DroidEv3 droidEv3;

    private TextView voltageTV;
    private TextView currentDrawTV;
    private TextView touchSensorTV;
    private TextView irSensorTV;
    private TextView colorSensorTV;
    private Button aboutBTN;
    private Switch sensorStatusSWBTN;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;
    private final String[] ledPatterns = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Spinner ledPatternSp;
    private boolean ledFlag;
    private Timer timer;
    private boolean sensorsStatus = false;
    private final String OPEN_SENSORS = "init";
    private final String CLOSE_SENSORS = "close";


    public static InfoFragment newInstance(int sectionNumber) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DrawerActivity.KEY_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DrawerActivity) activity).onSectionAttached(
                getArguments().getInt(DrawerActivity.KEY_SECTION_NUMBER));
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
        //new CreateInfoTask().execute(OPEN_SENSORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
/*        if (timer != null) {
            timer.cancel();
            timer = null;
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        if (sensorsStatus) {
            new CreateInfoTask().execute(CLOSE_SENSORS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        droidEv3 = getDroidEv3();
        droidEv3.initPower();
        droidEv3.initLed();
        bindView(view);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ledPatterns);
        ledPatternSp.setAdapter(spinnerArrayAdapter);
        setListeners();
    }

    private void bindView(View view) {
        voltageTV = (TextView) view.findViewById(R.id.fragment_info_voltage_tv);
        currentDrawTV = (TextView) view.findViewById(R.id.fragment_info_currentdraw_tv);
        touchSensorTV = (TextView) view.findViewById(R.id.fragment_info_touch_sensor_tv);
        irSensorTV = (TextView) view.findViewById(R.id.fragment_info_ir_sensor_tv);
        colorSensorTV = (TextView) view.findViewById(R.id.fragment_info_color_sensor_tv);
        ledPatternSp = (Spinner) view.findViewById(R.id.fragment_info_led_pattern_sp);
        aboutBTN = (Button) view.findViewById(R.id.fragment_info_about_btn);
        sensorStatusSWBTN = (Switch) view.findViewById(R.id.fragment_info_sensors_status_sw);
        progressBar1 = (ProgressBar) view.findViewById(R.id.fragment_info_progressBar1);
        progressBar2 = (ProgressBar) view.findViewById(R.id.fragment_info_progressBar2);
        progressBar3 = (ProgressBar) view.findViewById(R.id.fragment_info_progressBar3);
        progressBar4 = (ProgressBar) view.findViewById(R.id.fragment_info_progressBar4);
        progressBar5 = (ProgressBar) view.findViewById(R.id.fragment_info_progressBar5);
    }

    private void setListeners() {
        ledPatternSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected parent = " + parent.getSelectedItemPosition());
                if (parent.getSelectedItemPosition() == 0 && !ledFlag) {
                    ledPatternSp.setSelection(DroidEv3.lastLedPattern);
                    ledFlag = true;
                }
                if (droidEv3 != null) {
                    droidEv3.setLEDPattern(parent.getSelectedItemPosition());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        aboutBTN.setOnClickListener(this);
        sensorStatusSWBTN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.d(TAG, "The toggle is enabled");
                    if (droidEv3 != null) {
                        setVisibilityProgressBars(View.VISIBLE);
                        setVisibilityTextView(View.INVISIBLE);
                        new CreateInfoTask().execute(OPEN_SENSORS);
                    }
                } else {
                    // The toggle is disabled
                    Log.d(TAG, "The toggle is disabled");
                    if (droidEv3 != null) {
                        setVisibilityProgressBars(View.VISIBLE);
                        setVisibilityTextView(View.INVISIBLE);
                        new CreateInfoTask().execute(CLOSE_SENSORS);
                        clearTextsTV();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_info_about_btn:
                Log.d(TAG, "Acerca de........");
                break;
        }
    }

    private void timerInfo() {
        // Clase en la que está el código a ejecutar
        TimerTask timerTask = new TimerTask() {
            public void run() {
                // Aquí el código que queremos ejecutar.
                updateInfo();
            }
        };
        // Aquí se pone en marcha el timer cada segundo.
        timer = new Timer();
        // Dentro de 0 milisegundos avísame cada 1000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void updateInfo() {
        if (sensorsStatus) {
            droidEv3.fetchPowerInfo(this);
        }
    }

    @Override
    public void currentPowerInfo(int voltageMV, float currentDrawA, int touchSample, int irSample, int colorSample) {
        if (!isVisible()) return;
        Log.d(TAG, "voltaje = " + voltageMV + " consumo actual = " + currentDrawA + " touchSample = " + touchSample + " irSample = " + irSample + "colorSample = " + colorSample);
        voltageTV.setText(String.format(getResources().getString(R.string.voltage), voltageMV));
        currentDrawTV.setText(String.format(getResources().getString(R.string.current_draw), currentDrawA));
        touchSensorTV.setText(Integer.toString(touchSample));
        colorSensorTV.setText(Utils.getColorSt(getActivity(), colorSample));
        if (irSample > 55) {
            irSensorTV.setText("∞");
        } else {
            irSensorTV.setText(Integer.toString(irSample));
        }
    }

    private void setVisibilityProgressBars(int visibility) {
        progressBar1.setVisibility(visibility);
        progressBar2.setVisibility(visibility);
        progressBar3.setVisibility(visibility);
        progressBar4.setVisibility(visibility);
        progressBar5.setVisibility(visibility);
    }

    private void setVisibilityTextView(int visibility) {
        voltageTV.setVisibility(visibility);
        currentDrawTV.setVisibility(visibility);
        touchSensorTV.setVisibility(visibility);
        colorSensorTV.setVisibility(visibility);
        irSensorTV.setVisibility(visibility);
    }

    private void clearTextsTV() {
        voltageTV.setText(getString(R.string.off));
        currentDrawTV.setText(getString(R.string.off));
        touchSensorTV.setText(getString(R.string.off));
        colorSensorTV.setText(getString(R.string.off));
        irSensorTV.setText(getString(R.string.off));
    }

    private class CreateInfoTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Log.d(TAG, "doInBackground() params = " + params[0]);
            if (params[0].equals(OPEN_SENSORS) && !sensorsStatus) {
                Log.d(TAG, "doInBackground() Open Sensors");
                droidEv3.openSensors();
                sensorsStatus = true;
            }
            if (params[0].equals(CLOSE_SENSORS) && sensorsStatus) {
                Log.d(TAG, "doInBackground() Close Sensors");
                droidEv3.closeSensors();
                sensorsStatus = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute sensorsStatus = " + sensorsStatus);
            if (sensorsStatus && timer == null) {
                timerInfo();
            } else {
                if (timer != null) {
                    timer.cancel();//Comprobar que no falla por si se cierran antes de que pare el timer....
                    timer = null;
                }
            }
            setVisibilityProgressBars(View.INVISIBLE);
            setVisibilityTextView(View.VISIBLE);
        }
    }
}
