package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Application;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

/**
 * The Clinical Trial Client is the global application that all other activity and fragments use
 * to access the model and state machine.
 */
public class ClinicalTrialClient extends Application {
    private ClinicalTrialModel model;
    private ClinicalTrialStateMachine machine;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            String dbName = getResources().getString(R.string.trial_name);
            model = new ClinicalTrialModel(dbName, getApplicationContext().getFilesDir().toString());
            // Ensure we have a default clinic
            Clinic defaultClinic = model.getDefaultClinic();

            machine = new ClinicalTrialStateMachine(this);
        } catch (TrialCatalogException e) {

        }
    }

    /**
     *
     * @return returns the instance of the model.
     */
    public ClinicalTrialModel getModel() {
        return model;
    }

    /**
     *
     * @return returns the instance of the state machine.
     */
    public ClinicalTrialStateMachine getMachine() {
        return machine;
    }
}
