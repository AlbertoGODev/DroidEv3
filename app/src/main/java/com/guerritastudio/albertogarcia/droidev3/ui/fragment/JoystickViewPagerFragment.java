package com.guerritastudio.albertogarcia.droidev3.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseFragment;
import com.guerritastudio.albertogarcia.droidev3.ui.activity.DrawerActivity;
import com.guerritastudio.albertogarcia.droidev3.widget.RegisterViewPager;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoystickViewPagerFragment extends BaseFragment implements AnalogJoystickFragment.PagerEnabled {

    private RegisterViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    public static JoystickViewPagerFragment newInstance(int sectionNumber) {
        JoystickViewPagerFragment fragment = new JoystickViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DrawerActivity.KEY_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public JoystickViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((DrawerActivity) activity).onSectionAttached(
                getArguments().getInt(DrawerActivity.KEY_SECTION_NUMBER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_joystick_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), this);

        viewPager = (RegisterViewPager) view.findViewById(R.id.fragment_joystick_pager);
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public void enable() {
        viewPager.setPagingEnabled(true);
    }

    @Override
    public void disable() {
        viewPager.setPagingEnabled(false);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private WeakReference<AnalogJoystickFragment.PagerEnabled> fragment;

        public ViewPagerAdapter(FragmentManager fm, AnalogJoystickFragment.PagerEnabled fragment) {
            super(fm);
            this.fragment = new WeakReference<AnalogJoystickFragment.PagerEnabled>(fragment);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DigitalJoystickFragment.newInstance();
                case 1:
                    AnalogJoystickFragment fragment = AnalogJoystickFragment.newInstance();
                    fragment.setDelegate(this.fragment.get());
                    return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_strip_digital_joystick);
                case 1:
                    return getString(R.string.title_strip_analog_stick);
            }
            return "";
        }
    }
}
