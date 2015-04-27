package com.guerritastudio.albertogarcia.droidev3.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.IconButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.joanzapata.android.iconify.Iconify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;


public class SearchEv3DialogFragment extends DialogFragment implements View.OnClickListener{

    public static final String TAG = SearchEv3DialogFragment.class.getSimpleName();
    private ListView listView;
    private TextView notFoundTV;
    private BrickInfo[] devices;
    private SearchEv3Task searchEv3Task;
    private ProgressBar progressBar;
    private IconButton refreshIcBtn;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        // LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_search_ev3_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        bindView(view);
        setListeners();
        searchEv3Task = new SearchEv3Task();
        searchEv3();
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void bindView(View view) {
        listView = (ListView) view.findViewById(R.id.fragment_search_dialog_lv);
        notFoundTV = (TextView) view.findViewById(R.id.fragment_search_ev3_not_found_tv);
        progressBar = (ProgressBar) view.findViewById(R.id.searching_ev3_pbar);
        refreshIcBtn = (IconButton) view.findViewById(R.id.searching_ev3_refresh_btn);
    }
    private void setListeners(){
        refreshIcBtn.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void searchEv3() {
        if (searchEv3Task.getStatus() == AsyncTask.Status.FINISHED){
            searchEv3Task.cancel(true);
            searchEv3Task = new SearchEv3Task();
        }
        progressBar.setVisibility(View.VISIBLE);
        refreshIcBtn.setVisibility(View.GONE);
        searchEv3Task.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searching_ev3_refresh_btn:
                Log.d(TAG,"onClick() searching_ev3_refresh_btn");
                searchEv3();
                return;
        }
    }


    private class SearchEv3Task extends AsyncTask<Void, Void, BrickInfo[]> {

        @Override
        protected BrickInfo[] doInBackground(Void... params) {
            try {
                devices = null;
                devices = BrickFinder.discover();
                Log.e("SearchTask() ", " devices:" + devices.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return devices;
        }

        @Override
        protected void onPostExecute(BrickInfo[] brickInfos) {
            super.onPostExecute(brickInfos);
            Log.e(TAG,"onPostExecute() ");
            if (devices != null) {
                SearchListAdapter adapter = new SearchListAdapter();
                adapter.setListEV3(brickInfoToList());
                listView.setAdapter(adapter);
            }else {
                notFoundTV.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
            refreshIcBtn.setVisibility(View.VISIBLE);
        }
    }

    private List<BrickInfo> brickInfoToList() {
        List<BrickInfo> list = new ArrayList<>();
        for (int i = 0; i < devices.length; i++) {
            list.add(devices[i]);
        }
        return list;
    }

    private class SearchListAdapter extends BaseAdapter {

        private List<BrickInfo> listEV3;

        public void setListEV3(List<BrickInfo> listEV3) {
            this.listEV3 = listEV3;
        }

        @Override
        public int getCount() {
            return listEV3.size();
        }

        @Override
        public BrickInfo getItem(int position) {
            return listEV3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.row_brick_info, parent, false);
            }

            BrickInfo brickInfo = getItem(position);

            Log.e(TAG, " Brick found: Name: " + brickInfo.getName() + ", IpAddress: " + brickInfo.getIPAddress() + " Type: " + brickInfo.getType());

            TextView nameTV = (TextView) convertView.findViewById(R.id.row_brick_info_name);
            TextView ipAddressTv = (TextView) convertView.findViewById(R.id.row_brick_info_ip_address);

            nameTV.setText(brickInfo.getName());
            ipAddressTv.setText(brickInfo.getIPAddress());

            return convertView;
        }
    }
}
