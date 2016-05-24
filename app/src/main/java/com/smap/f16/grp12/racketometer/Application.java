package com.smap.f16.grp12.racketometer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.smap.f16.grp12.racketometer.services.PerformanceService;

/**
 * Created by bjornsorensen on 24/05/16.
 */
public class Application extends android.app.Application {
    private boolean bound = false;
    private PerformanceService performanceService;

    @Override
    public void onCreate() {
        super.onCreate();

        PerformanceService.bindService(this, serviceConnection);
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
            performanceService.clearSessions();
            PerformanceService.unbindService(getBaseContext(), serviceConnection);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
