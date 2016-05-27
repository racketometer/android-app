package com.smap.f16.grp12.racketometer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

/**
 * Helper class to enable detection of network and internet connectivity.
 */
public class ConnectivityHelper {
    public static final String INTERNET_STATUS
            = "com.smap.f16.grp12.racketometer.utils.ConnectivityHelper.INTERNET_STATUS";
    public static final String CONNECTION_STATUS
            = "com.smap.f16.grp12.racketometer.utils.ConnectivityHelper.CONNECTION_STATUS";
    public static final String EXTRA_STATUS
            = "com.smap.f16.grp12.racketometer.utils.ConnectivityHelper.EXTRA_STATUS";

    private final Context context;

    public ConnectivityHelper(Context context) {
        this.context = context;
    }

    /**
     * Test internet connectivity by pinging Google DNS.
     * Broadcasts status with INTERNET_STATUS.
     * Inspired by: http://stackoverflow.com/a/27312494/5324369
     *
     * @return True if connected. Otherwise false.
     */
    public boolean isOnline() {
        boolean isOnline = false;

        Runtime runtime = Runtime.getRuntime();

        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            isOnline = exitValue == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        broadcastInternetStatus(isOnline);

        return isOnline;
    }

    /**
     * Detect if device is connected to a network.
     *
     * @return True if connected or connecting. Otherwise false.
     */
    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean isConnected = info != null && info.isConnected();

        broadcastConnectionStatus(isConnected);
        return isConnected;
    }

    /**
     * Broadcast connection status.
     * @param isConnected The connection status.
     */
    private void broadcastConnectionStatus(Boolean isConnected) {
        Intent intent = new Intent(CONNECTION_STATUS);
        intent.putExtra(EXTRA_STATUS, isConnected);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastManager.sendBroadcast(intent);
    }

    /**
     * Broadcast internet status.
     *
     * @param isOnline The online status.
     */
    private void broadcastInternetStatus(Boolean isOnline) {
        Intent intent = new Intent(INTERNET_STATUS);
        intent.putExtra(EXTRA_STATUS, isOnline);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastManager.sendBroadcast(intent);
    }
}
