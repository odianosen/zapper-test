package com.odi.android.zappertest.events;

import com.odi.android.zappertest.model.Person;

import java.util.List;

/**
 * Created by Odi on 2015-05-07.
 */
public class PersonListLoadedEvent {
    private List<Person> personList;

    public PersonListLoadedEvent(List<Person> personList) {
        this.personList = personList;
    }

    public List<Person> getPersonList() {
        return personList;
    }
}
