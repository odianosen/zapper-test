package com.odi.android.zappertest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.odi.android.zappertest.R;
import com.odi.android.zappertest.events.BusProvider;
import com.odi.android.zappertest.events.PersonSelectedEvent;
import com.squareup.otto.Subscribe;

/**
 * An activity representing a list of Persons. This activity
 * has different presentations for portrait and landscape orientations.
 * Portrait - The activity presents a list of persons
 * Landscape - the activity presents the list of persons and
 * person details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PersonListFragment} and the person details
 * (if present) is a {@link PersonDetailFragment}.
 *
 * Created by Odi on 2015-05-07.
 */
public class PersonActivity  extends ActionBarActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_main);

        // If this view is present, then the
        // activity should be in two-pane mode.
        if (findViewById(R.id.person_detail_container) != null) {
            mTwoPane = true;
        }

        setupFragmentsIntoContainers();
        setupToolbar();
    }

    /**
     * Set up the Toolbar that will server as action bar
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Zapper Test!");
    }

    /**
     * Places List and detail fragments into their appropriate containers.
     */
    private void setupFragmentsIntoContainers() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.person_list_container);
        if (fragment == null) {
            //Init. Create a new list fragment and add to the list container.
            fragment = PersonListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.person_list_container, fragment)
                    .commit();
        } else {
            //When its 2-pane, we don't want the detail fragment showing in the list container...
            if (mTwoPane && fragment instanceof PersonDetailFragment) {
                //... so we remove the detail fragment from the list container (leaving only the list fragment)...
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment)
                        .commit();
                getSupportFragmentManager().executePendingTransactions();

                //.. and then we add the detail fragment to the detail container (which must exist cos we are in 2-pane mode!).
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.person_detail_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    //** Event subscriptions ***
    /**
     * Call back method for when a Person is selected from the list.
     * We need to init a detail fragment to display the details of the selected person.
     *
     * @param event
     */
    @Subscribe
    public void personSelected(final PersonSelectedEvent event) {
        long personId = event.getSelectedPersonId();
        PersonDetailFragment detailFragment = PersonDetailFragment.newInstance(personId);

        //Put the detail fragment in the right container - depending on whether we are in 2-pane or not.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mTwoPane) {
            transaction.replace(R.id.person_detail_container, detailFragment);
        } else {
            transaction.replace(R.id.person_list_container, detailFragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
