package com.guerritastudio.albertogarcia.droidev3.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guerritastudio.albertogarcia.droidev3.R;

import java.util.List;

import lejos.hardware.BrickInfo;

/**
 * Created by AlbertoGarcia on 28/4/15.
 */

public class SearchEv3DialogFragmentListAdapter extends BaseAdapter {

    private static final String TAG = SearchEv3DialogFragmentListAdapter.class.getSimpleName();
    private List<BrickInfo> listEV3;
    private FragmentActivity fragmentActivity;

    public SearchEv3DialogFragmentListAdapter(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setListEV3(List<BrickInfo> listEV3) {
        this.listEV3 = listEV3;
        this.notifyDataSetChanged();
    }

    public void clearList() {
        if (listEV3 != null) {
            listEV3.clear();
            this.notifyDataSetChanged();
        }
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
            convertView = LayoutInflater.from(fragmentActivity.getApplicationContext()).inflate(R.layout.row_brick_info, parent, false);
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
