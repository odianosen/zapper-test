package com.odi.android.zappertest.service;

import com.odi.android.zappertest.model.Person;
import com.odi.android.zappertest.model.PersonDetail;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Odi on 2015-05-07.
 */
public interface PersonRestClient {

    @GET("/person/list")
    void getPersonList(final Callback<List<Person>> listCallback);

    @GET("/person/{id}")
    void getPersonDetail(@Path("id") final Long personId, final Callback<PersonDetail> detailCallback);
}
