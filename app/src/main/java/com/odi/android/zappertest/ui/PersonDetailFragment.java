package com.odi.android.zappertest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.odi.android.zappertest.events.DetailRequestFailureEvent;
import com.odi.android.zappertest.service.PersonService;
import com.odi.android.zappertest.R;
import com.odi.android.zappertest.events.BusProvider;
import com.odi.android.zappertest.events.PersonDetailLoadedEvent;
import com.odi.android.zappertest.model.Person;
import com.squareup.otto.Subscribe;

/**
 * A fragment representing a single Person detail screen.
 */
public class PersonDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PERSON_ID = "person_id";
    private Person selectedPerson;
    private ViewGroup detailLayout;
    private ProgressBar progressBar;

    /**
     * Mandatory empty constructor
     */
    public PersonDetailFragment() {
    }

    public static PersonDetailFragment newInstance(long personId) {
        PersonDetailFragment fragment = new PersonDetailFragment();

        Bundle detailArguments = new Bundle();
        detailArguments.putLong(ARG_PERSON_ID, personId);

        fragment.setArguments(detailArguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PERSON_ID)) {
            long personId = getArguments().getLong(ARG_PERSON_ID);
            selectedPerson = PersonService.getInstance().getCachedPerson(personId);
            if (selectedPerson.getDetail() == null) { //Then we need to make a network request
                PersonService.getInstance().makePersonDetailRequest(personId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);
        detailLayout = (ViewGroup) rootView.findViewById(R.id.person_detail_layout);
        progressBar = (ProgressBar) detailLayout.findViewById(R.id.person_detail_progress);

        if (selectedPerson.getDetail() != null) {
            progressBar.setVisibility(View.GONE);
            displayPersonDetails();
        }
        return rootView;
    }

    /**
     * Populate the UI with our person!
     */
    private void displayPersonDetails() {
        TextView ageView = (TextView) detailLayout.findViewById(R.id.txt_person_detail_age);
        ageView.setText(selectedPerson.getDetail().getAge() + "");

        TextView countryView = (TextView) detailLayout.findViewById(R.id.txt_person_detail_country);
        countryView.setText(selectedPerson.getDetail().getCountry());

        TextView descView = (TextView) detailLayout.findViewById(R.id.txt_person_detail_description);
        descView.setText(selectedPerson.getDetail().getDescription());

        TextView jobView = (TextView) detailLayout.findViewById(R.id.txt_person_detail_job);
        jobView.setText(selectedPerson.getDetail().getJob());

        TextView addressView = (TextView) detailLayout.findViewById(R.id.txt_person_detail_address);
        addressView.setText(selectedPerson.getDetail().getAddress());

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    //***Event Subscriptions ****
    /**
     * Call back method for when the selected person's details have been asynchronously loaded.
     * @param event
     */
    @Subscribe
    public void personDetailLoaded(final PersonDetailLoadedEvent event) {
        selectedPerson = event.getPerson();
        progressBar.setVisibility(View.GONE);
        displayPersonDetails();
    }

    @Subscribe
    public void displayError(final DetailRequestFailureEvent failureEvent) {
        progressBar.setVisibility(View.GONE);
        String reason = failureEvent.getReason();
        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
    }

}
