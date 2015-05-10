package com.odi.android.zappertest.model;

import java.io.Serializable;

/**
 * Created by Odi on 2015-05-07.
 */
public class Person implements Serializable {
    private long id;
    private String name;
    private PersonDetail detail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonDetail getDetail() {
        return detail;
    }

    public void setDetail(PersonDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
