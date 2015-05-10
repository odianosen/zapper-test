package com.odi.android.zappertest.events;

import com.squareup.otto.Bus;

/**
 * Created by Odi on 2015-02-04.
 */
public class BusProvider {
    private static Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }

    private BusProvider() {
    }
}
