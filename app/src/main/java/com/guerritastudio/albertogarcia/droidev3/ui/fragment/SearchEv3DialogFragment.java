package com.guerritastudio.albertogarcia.droidev3.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.IconButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.ui.adapter.SearchEv3DialogFragmentListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;


public class SearchEv3DialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = SearchEv3DialogFragment.class.getSimpleName();
    private ListView listView;
    private TextView notFoundTV;
    private BrickInfo[] devices;
    private SearchEv3Task searchEv3Task;
    private ProgressBar progressBar;
    private IconButton refreshIcBtn;
    private SearchEv3DialogFragmentListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    // Use this instance of the interface to deliver action events
    private SearchEv3DialogListener mListener;


    // Override the Fragment.onAttach() method to instantiate the SearchEv3DialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the SearchEv3DialogListener so we can send events to the host
            mListener = (SearchEv3DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SearchEv3DialogListener");
        }
    }

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
        searchEv3();

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                mListener.onDialogNegativeClick();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void bindView(View view) {
        searchEv3Task = new SearchEv3Task();
        adapter = new SearchEv3DialogFragmentListAdapter(getActivity());
        listView = (ListView) view.findViewById(R.id.fragment_search_dialog_lv);
        notFoundTV = (TextView) view.findViewById(R.id.fragment_search_ev3_not_found_tv);
        progressBar = (ProgressBar) view.findViewById(R.id.searching_ev3_pbar);
        refreshIcBtn = (IconButton) view.findViewById(R.id.searching_ev3_refresh_btn);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_search_dialog_SRL);
    }

    private void setListeners() {
        refreshIcBtn.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void searchEv3() {
        if (searchEv3Task.getStatus() == AsyncTask.Status.FINISHED) {
            searchEv3Task.cancel(true);
            searchEv3Task = new SearchEv3Task();
        }
        adapter.clearList();
        notFoundTV.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        refreshIcBtn.setVisibility(View.GONE);
        searchEv3Task.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searching_ev3_refresh_btn:
                Log.d(TAG, "onClick() searching_ev3_refresh_btn");
                searchEv3();
                return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onDialogPositiveClick(adapter.getItem(position));
        dismiss();
    }


    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh ");
        searchEv3();
        swipeRefreshLayout.setRefreshing(false);
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface SearchEv3DialogListener {
        public void onDialogPositiveClick(BrickInfo brickInfo);

        public void onDialogNegativeClick();
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
        protected void onPostExecute(BrickInfo[] bricksInfo) {
            super.onPostExecute(bricksInfo);
            Log.d(TAG, "onPostExecute() ");
            if (devices != null) {
                adapter.setListEV3(brickInfoToList());
                listView.setAdapter(adapter);
            } else {
                notFoundTV.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
            refreshIcBtn.setVisibility(View.VISIBLE);
        }

        private List<BrickInfo> brickInfoToList() {
            List<BrickInfo> list = new ArrayList<>();
            for (int i = 0; i < devices.length; i++) {
                list.add(devices[i]);
            }
            return list;
        }
    }
}
