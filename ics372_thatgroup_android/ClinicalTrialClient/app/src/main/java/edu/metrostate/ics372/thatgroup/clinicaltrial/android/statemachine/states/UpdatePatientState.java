package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.PatientActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UpdatePatientState extends PatientState {

    public UpdatePatientState(ClinicalTrialStateMachine machine, Context context, Patient patient) {
        super(machine, context);

        Activity act = getFromActivity();
        this.patient = patient;

        if (patient != null && act != null) {
            Intent intent = new Intent(act, PatientActivity.class);
            intent.putExtra(context.getResources().getString(R.string.intent_update_patient), patient);
            act.startActivityForResult(intent, UPDATE_PATIENT);
        }
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_ERROR:
                machine.transition(new ClinicErrorState(machine, getCurrentActivity()), true);
                break;
            case ON_OK:
                Activity current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(
                            getContext().getResources().getString(R.string.intent_updated_or_added),
                            patient);
                    getCurrentActivity().setResult(RESULT_OK, returnIntent);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    getCurrentActivity().setResult(RESULT_CANCELED);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
        }
    }

    @Override
    public void onCleanup() {

    }

    @Override
    public void onSave() {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean canAddReading() {
        return true;
    }

    @Override
    public boolean canViewReadings() {
        return hasReadings();
    }

    @Override
    public boolean hasReadings() {
        boolean answer = false;
        try {
            answer = getMachine().getApplication().getModel().hasReadings(patient);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }

        return answer;
    }
}

