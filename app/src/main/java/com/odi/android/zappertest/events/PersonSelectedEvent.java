package com.odi.android.zappertest.events;

/**
 * Created by Odi on 2015-05-08.
 */
public class PersonSelectedEvent {

    private long selectedPersonId;

    public PersonSelectedEvent(long selectedPersonId) {
        this.selectedPersonId = selectedPersonId;
    }

    public long getSelectedPersonId() {
        return selectedPersonId;
    }
}
