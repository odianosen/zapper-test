package com.odi.android.zappertest.service;

import com.odi.android.zappertest.events.BusProvider;
import com.odi.android.zappertest.events.DetailRequestFailureEvent;
import com.odi.android.zappertest.events.ListRequestFailureEvent;
import com.odi.android.zappertest.events.PersonDetailLoadedEvent;
import com.odi.android.zappertest.events.PersonListLoadedEvent;
import com.odi.android.zappertest.model.Person;
import com.odi.android.zappertest.model.PersonDetail;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 'Controller' class.
 *
 * Created by Odi on 2015-05-07.
 */
public class PersonService  {
    private static PersonService instance;
    private PersonRestClient restClient;
    /**
     * This list serves as a cache to prevent unnecessary network calls
     */
    private List<Person> personList = null;

    public static PersonService getInstance() {
        if(instance == null) {
            instance = new PersonService();
        }
        return instance;
    }

    private PersonService() {
        this.restClient = PersonRestClientCreator.createClient();
    }

    public List<Person> getCachedPersonList() {
        return personList;
    }

    /**
     * Search throw the cache list for a Person with this id
     * Return null if none found
     * @param personId
     * @return
     */
    public Person getCachedPerson(final Long personId) {
        for (Person p : personList) {
            if (p.getId() == personId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Call the Rest API asynchronously for the list of Persons
     */
    public void makePersonListRequest() {
        restClient.getPersonList(new Callback<List<Person>>() {
            @Override
            public void success(List<Person> persons, Response response) {
                personList = persons;
                final PersonListLoadedEvent listLoadedEvent = new PersonListLoadedEvent(persons);
                BusProvider.getBus().post(listLoadedEvent);
            }

            @Override
            public void failure(RetrofitError error) {
                final String reason = error.getKind().name() + ": Error retrieving Person List.";
                ListRequestFailureEvent failureEvent = new ListRequestFailureEvent(reason);
                BusProvider.getBus().post(failureEvent);
            }
        });
    }

    /**
     * Call the Rest API asynchronously to retrieve a Person's details
     */
    public void makePersonDetailRequest(final Long personId) {
        restClient.getPersonDetail(personId, new Callback<PersonDetail>() {
            @Override
            public void success(PersonDetail personDetail, Response response) {
                final Person person = getCachedPerson(personId);
                if (person != null) {  //Is this even possible? :-/
                    person.setDetail(personDetail);
                    final PersonDetailLoadedEvent event = new PersonDetailLoadedEvent(person);
                    BusProvider.getBus().post(event);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                final String reason = error.getKind().name() + ": Error retrieving Person detail.";
                DetailRequestFailureEvent failureEvent = new DetailRequestFailureEvent(reason);
                BusProvider.getBus().post(failureEvent);
            }
        });
    }

}
