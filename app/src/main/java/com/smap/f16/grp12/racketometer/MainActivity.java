package com.smap.f16.grp12.racketometer;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.smap.f16.grp12.racketometer.fragments.DetailsFragment;
import com.smap.f16.grp12.racketometer.fragments.HistoryFragment;
import com.smap.f16.grp12.racketometer.fragments.NoDataFragment;
import com.smap.f16.grp12.racketometer.fragments.OverviewFragment;
import com.smap.f16.grp12.racketometer.models.FragmentEnum;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.services.PerformanceService;
import com.smap.f16.grp12.racketometer.utils.ConnectivityHelper;
import com.smap.f16.grp12.racketometer.utils.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends BaseActivity
        implements HistoryFragment.OnListFragmentInteractionListener {

    private final String LOG = "MainActivity";

    private boolean bound = false;
    private RelativeLayout rllMain;
    private List<Session> sessions;

    private PerformanceService performanceService;
    private BroadcastReceiver performanceListener;
    private BroadcastReceiver internetListener;

    private TextView txtError;

    private SwipeRefreshLayout refreshLayout;

    private final String FRAGMENT = "fragment";
    private final String SESSION_ID = "session_id";
    private int shownFragment;
    private long sessionId;

    //region Life cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // facebook app tracking
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        getSavedInstanceData(savedInstanceState);

        initUiReferences();
        initGestureListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(FRAGMENT, shownFragment);
        if(shownFragment == FragmentEnum.DETAILS.getValue()) {
            outState.putLong(SESSION_ID, sessionId);
        }
    }

    private void getSavedInstanceData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        shownFragment = savedInstanceState.getInt(FRAGMENT);

        if(shownFragment == FragmentEnum.DETAILS.getValue()) {
            sessionId = savedInstanceState.getLong(SESSION_ID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initBroadcastListeners();
        PerformanceService.bindService(this, serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMainFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();

        removeBroadcastListeners();

        if (bound) {
            PerformanceService.unbindService(this, serviceConnection);
            bound = false;
        }
    }
    //endregion

    //region Fragment methods.
    /**
     * Init main fragment according to session data.
     */
    private void initMainFragment() {
        sessions = getSessions();

        if (sessions.size() == 0) {
            showNoDataFragment();
        } else {
            switch (shownFragment) {
                case 1:  showHistoryFragment(sessions);
                    break;
                case 2:
                    for(Session session : sessions) {
                        if(session.getId() == sessionId) {
                            showDetailsFragment(R.id.fragment_container, session);
                            break;
                        }
                    }
                    break;
                default: showOverviewFragment();
                    break;
            }
        }

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

    /**
     * Decide if shown fragment is of specified type.
     *
     * @param fragment The fragment to compare with.
     * @return True if fragments are the same type. Otherwise false.
     */
    private <T extends Fragment> boolean isFragment(Class<T> fragment) {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);

        return fragment != null && fragment.isAssignableFrom(currentFragment.getClass());
    }

    /**
     * Displays the History fragment.
     *
     * @param sessions The sessions to use.
     */
    private void showHistoryFragment(List<Session> sessions) {
        if (findViewById(R.id.fragment_container) == null) {
            return;
        }

        Fragment historyFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (historyFragment instanceof HistoryFragment) {
            return;
        }

        shownFragment = FragmentEnum.HISTORY.getValue();
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

        if (overviewFragment instanceof HistoryFragment) {
            getFragmentManager().popBackStack();
            return;
        }

        shownFragment = FragmentEnum.OVERVIEW.getValue();
        overviewFragment = OverviewFragment.newInstance(sessions);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, overviewFragment)
                .commit();
    }

    /**
     * Displays the No Data fragment.
     */
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
     * Displays the Details fragment.
     */
    private void showDetailsFragment(int id, Session session) {
        if (findViewById(R.id.fragment_container) == null) {
            return;
        }

        Fragment detailsFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (detailsFragment instanceof DetailsFragment) {
            return;
        }

        DetailsFragment fragment = DetailsFragment.newInstance(session);

        shownFragment = FragmentEnum.DETAILS.getValue();
        sessionId = session.getId();

        getFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .addToBackStack(null)
                .commit();
    }
    //endregion

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
     * Initialize references to UI elements.
     */
    private void initUiReferences() {
        txtError = (TextView) findViewById(R.id.txt_error);
        rllMain = (RelativeLayout) findViewById(R.id.rll_main);
    }

    /**
     * Init gesture listeners for changing fragments on swipes.
     */
    private void initGestureListener() {
        rllMain.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                if (isFragment(OverviewFragment.class)) {
                    showHistoryFragment(sessions);
                }
            }

            @Override
            public void onSwipeRight() {
                if (isFragment(HistoryFragment.class)) {
                    showOverviewFragment();
                }
            }
        });
    }

    /**
     * Add broadcast listeners.
     */
    private void initBroadcastListeners() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        performanceListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                newDataAvailable();
            }
        };
        localBroadcastManager.registerReceiver(performanceListener, new IntentFilter(PerformanceService.NEW_SESSION_DATA));

        internetListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                internetStatusAvailable(intent);
            }
        };
        localBroadcastManager.registerReceiver(internetListener, new IntentFilter(ConnectivityHelper.INTERNET_STATUS));
    }

    /**
     * Handle internet status change.
     * @param intent The intent.
     */
    private void internetStatusAvailable(Intent intent) {
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        boolean isOnline = intent.getBooleanExtra(ConnectivityHelper.EXTRA_STATUS, false);
        if(isOnline) {
            txtError.setVisibility(View.GONE);
            return;
        }

        txtError.setText(getResources().getText(R.string.no_internet));
        txtError.setVisibility(View.VISIBLE);
    }

    /**
     * Remove broadcast listeners.
     */
    private void removeBroadcastListeners() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

        localBroadcastManager.unregisterReceiver(performanceListener);
        localBroadcastManager.unregisterReceiver(internetListener);

        performanceListener = null;
        internetListener = null;
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
            initMainFragment();
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
            switch (shownFragment) {
                case 1:
                    shownFragment = 0;
                    break;
                case 2:
                    shownFragment = 1;
                    break;
                default:
                    break;
            }
            super.onBackPressed();
            initMainFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                showHistoryFragment(getSessions());
                break;
            case R.id.menu_refresh:
                onRefresh(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Callbacks
    /**
     * Request data from webAPI
     *
     * @param refreshLayout The refresh layout if any.
     */
    public void onRefresh(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        if (performanceService != null) {
            performanceService.getSessionsFromApi();
        }
    }

    @Override
    public void onSessionSelected(Session item) {
        showDetailsFragment(R.id.fragment_container, item);
    }

    /**
     * Handle new data broadcast reception.
     */
    private void newDataAvailable() {
        sessions = getSessions();

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout = null;
        } else {
            initMainFragment();
        }

        updateFragmentData();
    }
    //endregion
}
