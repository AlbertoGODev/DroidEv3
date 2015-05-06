package com.guerritastudio.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.command.GetPowerInfoCommand.OnPowerInfo;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;

import java.util.Timer;
import java.util.TimerTask;


public class InfoFragment extends BaseFragment implements OnPowerInfo {

    private static final String TAG = InfoFragment.class.getSimpleName();

    private DroidEv3 droidEv3;

    private TextView voltageTV;
    private TextView currentDrawTV;
    private Button updateBTN;
    private final String[] ledPatterns = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Spinner ledPatternSp;
    private Timer timer;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        droidEv3 = getDroidEv3();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (timer != null) {
            timer.cancel();
        }
        //droidEv3.closeSensors();
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
        bindView(view);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ledPatterns);
        ledPatternSp.setAdapter(spinnerArrayAdapter);
        if (droidEv3 != null) {
            setListeners();
            timerInfo();
        }
    }

    private void bindView(View view) {
        voltageTV = (TextView) view.findViewById(R.id.fragment_info_voltage_tv);
        currentDrawTV = (TextView) view.findViewById(R.id.fragment_info_currentdraw_tv);
        ledPatternSp = (Spinner) view.findViewById(R.id.fragment_info_led_pattern_sp);
        updateBTN = (Button) view.findViewById(R.id.fragment_info_update_btn);
    }

    private void setListeners() {
        ledPatternSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected parent = " + parent.getSelectedItem());
                droidEv3.setLEDPattern(Integer.parseInt(parent.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //To test sensors:
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick()");
                //droidEv3.initSensors();
                //droidEv3.getTouchSensor();
            }
        });
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
        droidEv3.fetchPowerInfo(this);
    }

    @Override
    public void currentPowerInfo(int voltageMV, float currentDrawA) {
        if (!isVisible()) return;
        Log.e(TAG, "voltaje = " + voltageMV + " consumo actual = " + currentDrawA);
        if (!this.isRemoving()) {
            voltageTV.setText(String.format(getResources().getString(R.string.voltage), voltageMV));
            currentDrawTV.setText(String.format(getResources().getString(R.string.current_draw), currentDrawA));
        }
    }
}
