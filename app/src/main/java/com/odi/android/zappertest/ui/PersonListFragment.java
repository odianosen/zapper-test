package com.odi.android.zappertest.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.odi.android.zappertest.R;
import com.odi.android.zappertest.events.ListRequestFailureEvent;
import com.odi.android.zappertest.service.PersonService;
import com.odi.android.zappertest.events.BusProvider;
import com.odi.android.zappertest.events.PersonListLoadedEvent;
import com.odi.android.zappertest.events.PersonSelectedEvent;
import com.odi.android.zappertest.model.Person;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Persons.
 */
public class PersonListFragment extends ListFragment {

    private List<Person> personList = new ArrayList<>();
    private ArrayAdapter<Person> personArrayAdapter;
    private ImageButton refreshButton;

    /**
     * Mandatory empty constructor.
     */
    public PersonListFragment() {
    }

    public static PersonListFragment newInstance() {
        return new PersonListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Using the default ArrayAdapter. Demo project, no need to create a fancier Adapter.
        personArrayAdapter = new ArrayAdapter<Person>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                personList);
        setListAdapter(personArrayAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView =  super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.fragment_person_list, container, false);

        refreshButton = (ImageButton) parent.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                setListShown(false);
                PersonService.getInstance().makePersonListRequest();
            }
        });

        parent.addView(listView, 0);
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If the list isn't already present in the in-memory cache, then request it via the network.
        final List<Person> cachedPersonList = PersonService.getInstance().getCachedPersonList();
        if (cachedPersonList == null) {
            setListShown(false); //Automatically causes a progress bar to be displayed
            PersonService.getInstance().makePersonListRequest();
        } else {
            showPersonList(cachedPersonList);
        }
    }

    /**
     * Reload the list and tell the adapter to refresh itself.
     * @param newList
     */
    private void showPersonList(List<Person> newList) {
        this.personList.clear();
        this.personList.addAll(newList);
        this.personArrayAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        //When a person has been clicked, notify the Activity via the event bus (Otto totally ROCKS!)
        PersonSelectedEvent selectEvent = new PersonSelectedEvent(personArrayAdapter.getItem(position).getId());
        BusProvider.getBus().post(selectEvent);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
    }

    //*** Event subscriptions ***
    /**
     * Call back method for when the list has been asynchronously loaded
     * @param event
     */
    @Subscribe
    public void personListLoaded(final PersonListLoadedEvent event) {
        List<Person> personList = event.getPersonList();
        if (personList != null) {
            showPersonList(personList);
        }
    }

    @Subscribe
    public void displayError(final ListRequestFailureEvent failureEvent) {
        //Show an empty list (and remove the progress bar)
        showPersonList(new ArrayList<Person>());

        //Show error and allow the user refresh
        String reason = failureEvent.getReason();
        Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();

        refreshButton.setVisibility(View.VISIBLE);
    }

}
