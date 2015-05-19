package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
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

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.app.ConstDroidEv3;
import com.guerritastudio.albertogarcia.droidev3.app.Utils;
import com.guerritastudio.albertogarcia.droidev3.command.GetSensorsInfoCommand.OnPowerInfo;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.io.IOException;
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
    private Spinner ledPatternSp;
    private Timer timer;
    private boolean sensorsStatus = false;
    private boolean ledFlag;


    public static InfoFragment newInstance(int sectionNumber) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstDroidEv3.KEY_SECTION_NUMBER, sectionNumber);
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
                getArguments().getInt(ConstDroidEv3.KEY_SECTION_NUMBER));
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
        //new InfoTask().execute(OPEN_SENSORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e(TAG, "onPause()");
/*        if (timer != null) {
            timer.cancel();
            timer = null;
        }*/
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        if (sensorsStatus) {
            new InfoTask().execute(ConstDroidEv3.CLOSE_SENSORS);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
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
        if (droidEv3 != null) {
            droidEv3.initPower();
            droidEv3.initLed();
        }
        bindView(view);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ConstDroidEv3.LED_PATTERNS);
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
                        new InfoTask().execute(ConstDroidEv3.OPEN_SENSORS);
                    }
                } else {
                    // The toggle is disabled
                    Log.d(TAG, "The toggle is disabled");
                    if (droidEv3 != null) {
                        setVisibilityProgressBars(View.VISIBLE);
                        setVisibilityTextView(View.INVISIBLE);
                        new InfoTask().execute(ConstDroidEv3.CLOSE_SENSORS);
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
        Log.d(TAG, "timerInfo()");
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
        timer.scheduleAtFixedRate(timerTask, 0, 2000);
    }

    private void updateInfo() {
        Log.d(TAG, "updateInfo with sensorStatus = " + sensorsStatus);
        if (sensorsStatus) {
            droidEv3.fetchSensorsInfo(this);
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

    private void setTextsTV(String text) {
        voltageTV.setText(text);
        currentDrawTV.setText(text);
        touchSensorTV.setText(text);
        colorSensorTV.setText(text);
        irSensorTV.setText(text);
    }

    private class InfoTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Log.d(TAG, "doInBackground() params = " + params[0]);
            if (params[0].equals(ConstDroidEv3.OPEN_SENSORS) && !sensorsStatus) {
                //Reconnect para asegurar la conexión.
                try {
                    droidEv3.disConnect();
                    droidEv3.reConnect();
                    droidEv3.initPower();
                    droidEv3.initLed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "doInBackground() Open Sensors");
                droidEv3.openSensors();
                sensorsStatus = true;
            }
            if (params[0].equals(ConstDroidEv3.CLOSE_SENSORS) && sensorsStatus) {
                sensorsStatus = false;
                timer.cancel();
                Log.d(TAG, "doInBackground() Close Sensors");
                droidEv3.closeSensors();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute sensorsStatus = " + sensorsStatus);
            if (!isVisible()) return;
            if (sensorsStatus) {
                if (timer == null) {
                    timerInfo();
                    setTextsTV("");
                }
            } else {
                if (timer != null) {
                    //timer.cancel();//Comprobar que no falla por si se cierran antes de que pare el timer....PUESTO ANTES.........
                    Log.d(TAG, "onPostExecute timer canceled");
                    timer = null;
                    setTextsTV(getResources().getString(R.string.off));
                }
            }
            setVisibilityProgressBars(View.INVISIBLE);
            setVisibilityTextView(View.VISIBLE);
        }
    }
}
