package com.meetdesk.helper;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.meetdesk.R;

import java.util.HashMap;

/**
 * Created by ekobudiarto on 9/16/16.
 */
public class HelperApplication extends Application {
    private final String TRACKER_ID = "UA-88297429-1";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public HelperApplication() {
        super();
    }
    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(TRACKER_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.google_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }


    @Override
    public void onCreate() {
        MultiDex.install(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate();
    }
}
