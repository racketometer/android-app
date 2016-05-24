package com.smap.f16.grp12.racketometer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smap.f16.grp12.racketometer.fragments.HistoryFragment;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.services.PerformanceService;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements HistoryFragment.OnListFragmentInteractionListener {

    private final String LOG = "MainActivity";

    private boolean bound = false;
    private PerformanceService performanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadcastListener();
        PerformanceService.bindService(this, serviceConnection);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!bound) {
            return;
        }

        PerformanceService.unbindService(this, serviceConnection);
        bound = false;
    }

    /**
     * Handle new data broadcast reception.
     */
    private void newDataAvailable() {
        Log.i(LOG, "Broadcast received");
        // get new data from service
    }

    /**
     * Initialize broadcast listeners.
     */
    private void initBroadcastListener() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newDataAvailable();
            }
        }, new IntentFilter(PerformanceService.NEW_SESSION_DATA));
    }

    /**
     * ServiceConnection to performance service.
     */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PerformanceService.PerformanceServiceBinder binder =
                    (PerformanceService.PerformanceServiceBinder) service;

            performanceService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(MainActivity.this, "Action " + id + " pressed", Toast.LENGTH_SHORT).show();

        if(performanceService != null) {
            showHistoryFragment(R.id.fragment_container , performanceService.getSessions());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionSelected(Session item) {
        Toast.makeText(MainActivity.this, "MainActivity: session selected", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays the History fragment
     * @param id
     * @param sessions
     */
    private void showHistoryFragment(int id, List<Session> sessions) {
        if (findViewById(id) == null) {
            return;
        }
        List<Session> test = new ArrayList<>();
        Session testSession = new Session(1, DateTime.now(),1 , "this is a very ver yveru a fao long string eyeah", 222, 10, 10, 10, 50.5, 50.2);
        test.add(testSession);
        HistoryFragment fragment = HistoryFragment.newInstance(test);

        getFragmentManager()
                .beginTransaction()
                .add(id, fragment)
                .commit();
    }
}
