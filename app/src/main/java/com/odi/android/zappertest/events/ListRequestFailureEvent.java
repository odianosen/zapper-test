package com.odi.android.zappertest.events;

/**
 * Created by Odi on 2015-05-09.
 */
public class ListRequestFailureEvent {
    private String reason;

    public ListRequestFailureEvent(final String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
