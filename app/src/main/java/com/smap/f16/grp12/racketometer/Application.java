package com.smap.f16.grp12.racketometer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.smap.f16.grp12.racketometer.services.PerformanceService;

/**
 * Application extension to enable local cache initialization from WebAPI.
 */
public class Application extends android.app.Application {
    private PerformanceService performanceService;

    @Override
    public void onCreate() {
        super.onCreate();

        PerformanceService.bindService(this, serviceConnection);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PerformanceService.PerformanceServiceBinder binder =
                    (PerformanceService.PerformanceServiceBinder) service;

            performanceService = binder.getService();
            performanceService.clearSessions();
            PerformanceService.unbindService(getBaseContext(), serviceConnection);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            performanceService = null;
        }
    };
}
