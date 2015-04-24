package com.droidev3.albertogarcia.droidev3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.droidev3.albertogarcia.droidev3.R;
import com.droidev3.albertogarcia.droidev3.app.BaseActionBarActivity;
import com.droidev3.albertogarcia.droidev3.app.MyApp;
import com.droidev3.albertogarcia.droidev3.app.Utils;
import com.droidev3.albertogarcia.droidev3.model.DroidEv3;
import com.droidev3.albertogarcia.droidev3.task.ConnectionTask;

import java.util.regex.Matcher;


public class ConnectActivity extends BaseActionBarActivity implements View.OnClickListener {

    private static final String TAG = ConnectActivity.class.getSimpleName();

    private ConnectionTask droidConnectTask;

    private EditText ipAddressET;
    private Button connectBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        bindView();
        setListeners();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (droidConnectTask != null) {
            droidConnectTask.cancel(true);
            droidConnectTask = null;
        }
        this.disconnectDroidEv3();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_ev3_btn:
                /*//Para entrar sin conectar:
                startActivity(new Intent(ConnectActivity.this, DrawerActivity.class));*/
                connectWithEv3();
        }
    }

    private void bindView() {
        ipAddressET = (EditText) findViewById(R.id.connect_ipAddressET);
        ipAddressET.setFilters(Utils.getFilterIP());
        connectBTN = (Button) findViewById(R.id.connect_ev3_btn);

    }

    private void setListeners() {
        connectBTN.setOnClickListener(this);
        ipAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "setListeners()------->onEditorAction()");
                connectWithEv3();
                return false;
            }
        });
    }

    private void connectWithEv3() {
        String ip = ipAddressET.getText().toString();
        if (validateIP(ip)) {
            Log.d(TAG, "connectWithEv3() ipAddress: " + ip);
            if (!isTaskRun()) {
                droidConnectTask = buildTask();
                droidConnectTask.execute(ip);
                connectBTN.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(ConnectActivity.this, "conectando...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isTaskRun() {
        return droidConnectTask != null;
    }

    private void clearTask() {
        this.droidConnectTask = null;
    }

    private ConnectionTask buildTask() {
        ConnectionTask task = new ConnectionTask() {
            @Override
            public void onConnectedToDroid(DroidEv3 droidEv3) {
                connectBTN.setVisibility(View.VISIBLE);
                ConnectActivity.this.setDroidEv3(droidEv3);
                startActivity(new Intent(ConnectActivity.this, DrawerActivity.class));
                clearTask();
                finish();
            }

            @Override
            public void onConnectionException(Exception e) {
                connectBTN.setVisibility(View.VISIBLE);
                Toast.makeText(ConnectActivity.this, "onConnectionException", Toast.LENGTH_LONG).show();
                clearTask();
            }

            @Override
            public void onConnectionError() {
                connectBTN.setVisibility(View.VISIBLE);
                Toast.makeText(ConnectActivity.this, "onConnectionError", Toast.LENGTH_LONG).show();
                clearTask();
            }
        };
        return task;
    }


    private boolean validateIP(String ip) {

        if (ip.isEmpty()) {
            ipAddressET.setError(getResources().getString(R.string.form_empty_field));
            ipAddressET.requestFocus();
            return false;
        }

        Matcher matcher = Patterns.IP_ADDRESS.matcher(ip);
        if (!matcher.matches()) {
            Toast.makeText(this, getResources().getString(R.string.form_ip_error), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
}

