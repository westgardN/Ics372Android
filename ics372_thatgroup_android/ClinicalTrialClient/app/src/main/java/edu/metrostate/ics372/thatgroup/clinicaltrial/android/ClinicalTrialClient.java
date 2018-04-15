package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Application;
import android.content.Context;

public class ClinicalTrialClient extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        ClinicalTrialClient.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ClinicalTrialClient.context;
    }

}
