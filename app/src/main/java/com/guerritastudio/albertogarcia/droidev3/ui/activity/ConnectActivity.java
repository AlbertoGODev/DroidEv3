package com.guerritastudio.albertogarcia.droidev3.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseActionBarActivity;
import com.guerritastudio.albertogarcia.droidev3.app.Utils;
import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;
import com.guerritastudio.albertogarcia.droidev3.task.ConnectionTask;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.SearchEv3DialogFragment;

import java.util.regex.Matcher;

import lejos.hardware.BrickInfo;


public class ConnectActivity extends BaseActionBarActivity implements View.OnClickListener, SearchEv3DialogFragment.SearchEv3DialogListener {

    private static final String TAG = ConnectActivity.class.getSimpleName();

    private ConnectionTask droidConnectTask;

    private EditText ipAddressET;
    private Button connectBTN;
    private Button searchBTN;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        bindView();
        setListeners();

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (droidConnectTask != null) {
            droidConnectTask.cancel(true);
            droidConnectTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_ev3_btn:
                //Para entrar sin conectar:
                if (ipAddressET.getText().toString().equals("123")) {
                    Log.e(TAG, " Mode Demo");
                    startActivity(new Intent(ConnectActivity.this, DrawerActivity.class));
                } else {
                    connectWithEv3();
                }
                return;
            case R.id.search_ev3_btn:
                showSearchEv3Dialog();
        }
    }

    private void showSearchEv3Dialog() {
        DialogFragment dialogFragment = new SearchEv3DialogFragment();
        dialogFragment.show(getSupportFragmentManager(), SearchEv3DialogFragment.TAG);
    }

    private void bindView() {
        ipAddressET = (EditText) findViewById(R.id.connect_ipAddressET);
        ipAddressET.setFilters(Utils.getFilterIP());
        connectBTN = (Button) findViewById(R.id.connect_ev3_btn);
        searchBTN = (Button) findViewById(R.id.search_ev3_btn);
        progressBar = (ProgressBar) findViewById(R.id.connect_ev3_pbar);
    }

    private void setListeners() {
        connectBTN.setOnClickListener(this);
        searchBTN.setOnClickListener(this);
        ipAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "setListeners()------->onEditorAction()");
                //Para entrar sin conectar:
                if (ipAddressET.getText().toString().equals("123")) {
                    Log.e(TAG, " Mode Demo");
                    startActivity(new Intent(ConnectActivity.this, DrawerActivity.class));
                    finish();
                } else {
                    connectWithEv3();
                }
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
                hideButtons();
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
                showButtons();
                ConnectActivity.this.setDroidEv3(droidEv3);
                startActivity(new Intent(ConnectActivity.this, DrawerActivity.class));
                clearTask();
                finish();
            }

            @Override
            public void onConnectionException(Exception e) {
                showButtons();
                Toast.makeText(ConnectActivity.this, getResources().getString(R.string.form_ip_exception), Toast.LENGTH_LONG).show();
                clearTask();
            }

            @Override
            public void onConnectionError() {
                showButtons();
                Toast.makeText(ConnectActivity.this, getResources().getString(R.string.form_ip_not_discover), Toast.LENGTH_LONG).show();
                clearTask();
            }
        };
        return task;
    }

    private void showButtons() {
        connectBTN.setVisibility(View.VISIBLE);
        searchBTN.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void hideButtons() {
        connectBTN.setVisibility(View.GONE);
        searchBTN.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
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

    /*
    The dialog fragment receives a reference to this Activity through the
    Fragment.onAttach() callback, which it uses to call the following methods
    defined by the SearchEv3DialogFragment.SearchEv3DialogListener interface
    */

    @Override
    public void onDialogPositiveClick(BrickInfo brickInfo) {
        // User touched the dialog's positive button
        Log.d(TAG, "onDialogPositiveClick() brickInfo ipAddress: " + brickInfo.getIPAddress());
        ipAddressET.setText(brickInfo.getIPAddress());
    }

    @Override
    public void onDialogNegativeClick() {
        // User touched the dialog's negative button
        Log.e(TAG, "onDialogNegativeClick()");
    }
}

