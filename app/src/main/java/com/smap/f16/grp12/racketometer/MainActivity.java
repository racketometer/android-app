package com.smap.f16.grp12.racketometer;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.smap.f16.grp12.racketometer.fragments.HistoryFragment;
import com.smap.f16.grp12.racketometer.fragments.NoDataFragment;
import com.smap.f16.grp12.racketometer.fragments.OverviewFragment;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.services.PerformanceService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends BaseActivity
        implements HistoryFragment.OnListFragmentInteractionListener {

    private final String LOG = "MainActivity";

    private boolean bound = false;
    private List<Session> sessions;

    private PerformanceService performanceService;

    private BroadcastReceiver performanceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMainFragment();
    }

    private void initMainFragment() {
        sessions = getSessions();
        if (sessions.size() == 0) {
            showNoDataFragment();
        } else {
            showOverviewFragment();
        }
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

        removeBroadcastListener();

        if (bound) {
            PerformanceService.unbindService(this, serviceConnection);
            bound = false;
        }
    }

    /**
     * Handle new data broadcast reception.
     */
    private void newDataAvailable() {
        sessions = getSessions();
        initMainFragment();
        updateFragmentData();
    }

    /**
     * Update fragments with new session data.
     */
    private void updateFragmentData() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof OverviewFragment) {
            OverviewFragment overviewFragment = (OverviewFragment) fragment;
            overviewFragment.setSessions(sessions);
        }

        if (fragment instanceof HistoryFragment) {
            // set history fragment sessions
            Log.i(LOG, "history fragment showing");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                showHistoryFragment(getSessions());
                break;
            case R.id.menu_refresh:
                if (performanceService != null) {
                    performanceService.getSessionsFromApi();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionSelected(Session item) {
        Toast.makeText(MainActivity.this, "MainActivity: session selected", Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays the History fragment.
     *
     * @param sessions    The sessions to use.
     */
    private void showHistoryFragment(List<Session> sessions) {
        if (findViewById(R.id.fragment_container) == null) {
            return;
        }

        Fragment historyFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (historyFragment instanceof HistoryFragment) {
            return;
        }

        historyFragment = HistoryFragment.newInstance(sessions);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, historyFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Displays the Overview fragment.
     */
    private void showOverviewFragment() {
        if (findViewById(R.id.fragment_container) == null) {
            return;
        }

        Fragment overviewFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (overviewFragment instanceof OverviewFragment) {
            return;
        }

        overviewFragment = OverviewFragment.newInstance(sessions);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, overviewFragment)
                .commit();
    }

    private void showNoDataFragment() {
        if (findViewById(R.id.fragment_container) == null) {
            return;
        }

        Fragment noDataFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (noDataFragment instanceof NoDataFragment) {
            return;
        }

        noDataFragment = NoDataFragment.newInstance();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, noDataFragment)
                .commit();
    }

    /**
     * Get sessions from performance service or empty list.
     *
     * @return List of {@link Session}.
     */
    private List<Session> getSessions() {
        if (performanceService != null) {
            return performanceService.getSessions();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Add broadcast listener.
     */
    private void initBroadcastListener() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        performanceListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newDataAvailable();
            }
        };

        localBroadcastManager.registerReceiver(performanceListener, new IntentFilter(PerformanceService.NEW_SESSION_DATA));
    }

    /**
     * Remove broadcast listener.
     */
    private void removeBroadcastListener() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(performanceListener);
        performanceListener = null;
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
            performanceService.getSessionsFromApi();
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
}
