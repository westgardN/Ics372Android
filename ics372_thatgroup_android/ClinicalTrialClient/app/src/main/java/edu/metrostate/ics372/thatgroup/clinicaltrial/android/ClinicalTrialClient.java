package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Application;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicalTrialClient extends Application {
    private ClinicalTrialModel model;

    public void onCreate() {
        super.onCreate();
        try {
            model = new ClinicalTrialModel("android", getApplicationContext().getFilesDir().toString());
            Clinic clinic = model.getDefaultClinic();
            if (clinic != null) {
                clinic.getId();
            }

        } catch (TrialCatalogException e) {

        }
    }

    public ClinicalTrialModel getModel() {
        return model;
    }
}
