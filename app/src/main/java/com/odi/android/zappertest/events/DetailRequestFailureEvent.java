package com.odi.android.zappertest.events;

/**
 * Created by Odi on 2015-05-09.
 */
public class DetailRequestFailureEvent {
    private String reason;

    public DetailRequestFailureEvent(final String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
