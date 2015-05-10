package com.odi.android.zappertest.events;

import com.odi.android.zappertest.model.Person;

/**
 * Created by Odi on 2015-05-08.
 */
public class PersonDetailLoadedEvent {
    private Person person;

    public PersonDetailLoadedEvent(final Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
