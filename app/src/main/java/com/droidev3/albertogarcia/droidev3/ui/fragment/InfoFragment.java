package com.droidev3.albertogarcia.droidev3.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidev3.albertogarcia.droidev3.R;
import com.droidev3.albertogarcia.droidev3.ui.activity.DrawerActivity;


public class InfoFragment extends Fragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DrawerActivity) activity).onSectionAttached(
                getArguments().getInt(DrawerActivity.KEY_SECTION_NUMBER));
    }
}
