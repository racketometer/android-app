package com.smap.f16.grp12.racketometer.services;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.DatabaseHelper;
import com.smap.f16.grp12.racketometer.utils.performanceApi.PerformanceApi;
import com.smap.f16.grp12.racketometer.utils.performanceApi.SessionsReceivedCallback;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} for handling communication with Performance API
 * and local database cache.
 */

/**
 * Weather service. Collect weather data in regular intervals and
 * save it in a database for later use.
 */
public class PerformanceService extends Service implements SessionsReceivedCallback {
    public static final String NEW_SESSION_DATA =
            "com.smap.f16.grp12.racketometer.services.NEW_SESSION_DATA";

    private static final String LOG = "PerformanceService";

    private List<Session> sessions;

    private DatabaseHelper db;
    private final IBinder binder = new PerformanceServiceBinder();

    /**
     * Connect to service and create if needed.
     *
     * @param context The context to start the service from.
     * @param conn    The service connection to bind the service to.
     */
    public static void bindService(Context context, ServiceConnection conn) {
        Intent intent = new Intent(context, PerformanceService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind service connection.
     *
     * @param context The context to start the service from.
     * @param conn    The service connection to bind the service to.
     */
    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    /**
     * Clear sessions from database cache.
     */
    public void clearSessions() {
        db.clearSessions();
    }

    /**
     * Service binder providing access to the service instance.
     */
    public class PerformanceServiceBinder extends Binder {
        /**
         * Get current service instance.
         * @return The service instance.
         */
        public PerformanceService getService() {
            return PerformanceService.this;
        }
    }

    /**
     * Get sessions
     *
     * @return A {@link List} of {@link Session} or null if no data is available.
     */
    public List<Session> getSessions() {
        return sessions;
    }

    /**
     * Get Session by ID
     * @param id The Session id.
     * @return The Session or null if not found.
     */
    public Session getSession(int id) {
        for(Session session : sessions) {
            if(session.getId() == id) {
                return session;
            }
        }

        return null;
    }

    /**
     * Get sessions from past week.
     * @return The sessions.
     */
    public List<Session> getOverview() {
        List<Session> sessions = new ArrayList<>();

        DateTime twoWeeksAgo = new DateTime()
                .minusDays(14)
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);

        for(Session session : this.sessions) {
            if(twoWeeksAgo.isBefore(session.getDate())) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    /**
     * Get sessions from Performance API asynchronous.
     * Removes any existing sessions in database cache.
     */
    public void getSessionsFromApi() {
        db.clearSessions();
        PerformanceApi api = new PerformanceApi(this, this);
        api.execute();
    }


    @Override
    public IBinder onBind(Intent intent) {
        initialize();
        return binder;
    }

    @Override
    public void onDestroy() {
        db.closeDatabase();
        super.onDestroy();
    }

    /**
     * Start service operation.
     */
    private void initialize() {
        if(db == null) {
            db = new DatabaseHelper(this);
        }

        sessions = db.getSessions();
    }

    /**
     * Callback method when sessions are received.
     *
     * @param sessions The received sessions.
     */
    @Override
    public void sessionsReceived(List<Session> sessions) {
        if (sessions == null) {
            Log.i(LOG, "Sessions are null");
            //return;
            sessions = new ArrayList<>();
//            DateTime longAgo = new DateTime().minusDays(1);
//            sessions.add(new Session(longAgo, 1, "Dummy", 5, 4.4, 5.5, 6.6, 56.172183, 10.191971));
        }

        Log.i(LOG, "Sessions received");

        for (int i = 0; i < sessions.size(); i++) {
            db.createSession(sessions.get(i));
        }

        this.sessions = db.getSessions();

        broadcastNewSessions();
    }

    /**
     * Broadcast event on new sessions.
     */
    private void broadcastNewSessions() {
        Intent intent = new Intent(NEW_SESSION_DATA);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }
}
