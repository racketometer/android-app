package com.smap.f16.grp12.racketometer.utils.performanceApi;

import com.smap.f16.grp12.racketometer.models.Session;

import java.util.List;

public interface SessionsReceivedCallback {
    void sessionsReceived(List<Session> sessions);
}
