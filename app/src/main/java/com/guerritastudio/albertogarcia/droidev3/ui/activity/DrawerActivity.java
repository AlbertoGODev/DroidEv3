package com.guerritastudio.albertogarcia.droidev3.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.guerritastudio.albertogarcia.droidev3.R;
import com.guerritastudio.albertogarcia.droidev3.app.BaseActionBarActivity;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.InfoFragment;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.JoystickViewPagerFragment;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.StartFragment;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.NavigationDrawerFragment;
import com.guerritastudio.albertogarcia.droidev3.ui.fragment.TextToSpeechFragment;

public class DrawerActivity extends BaseActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = DrawerActivity.class.getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar toolbar;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        toolbar = (Toolbar) findViewById(R.id.drawer_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        onNavigationDrawerItemSelected(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");

        // getDroidEv3().closeSensors();//Hay que cerrarlos en la pregunta....
/*        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        disconnectDroidEv3();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {

            case 0:
                Log.d(TAG, "onNavigationDrawerItemSelected() = case 0 (click en cabecera)");
                fragmentManager.beginTransaction().replace(R.id.content_frame, StartFragment.newInstance(position)).commit();
                break;
            case 1:
                Log.d(TAG, "onNavigationDrawerItemSelected() = case 1");
                fragmentManager.beginTransaction().replace(R.id.content_frame, TextToSpeechFragment.newInstance(position)).commit();
                break;
            case 2:
                Log.d(TAG, "onNavigationDrawerItemSelected() = case 2");
                fragmentManager.beginTransaction().replace(R.id.content_frame, JoystickViewPagerFragment.newInstance(position)).commit();
                break;
            case 3:
                Log.d(TAG, "onNavigationDrawerItemSelected() = case 3");
                fragmentManager.beginTransaction().replace(R.id.content_frame, InfoFragment.newInstance(position)).commit();
                break;

        }
    }

    public void onSectionAttached(int number) {
        mTitle = getResources().getStringArray(R.array.menu_sections)[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.drawer, menu);
            restoreActionBar();//To setTitle
            return true;
        }

        getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.action_disconnect) {
            Log.d(TAG, "onOptionsItemSelected() action_disconnect");
            exitAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitAlertDialog();
    }

    private void exitAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.disconnect);
        dialog.setMessage(R.string.disconnect_dialog);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                try {
                    if (getDroidEv3() != null) {
                        //getDroidEv3().closeSensors();
                        //Thread.sleep(500);//Necessary to closeSensors().
                        //disconnectDroidEv3();
                    }
                    startActivity(new Intent(DrawerActivity.this, ConnectActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Canceled...
            }
        });
        dialog.show();
    }
}
