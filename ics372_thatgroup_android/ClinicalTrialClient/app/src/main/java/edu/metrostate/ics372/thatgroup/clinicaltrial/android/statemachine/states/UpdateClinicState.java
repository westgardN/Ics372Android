package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

import static android.app.Activity.RESULT_OK;

public class UpdateClinicState extends ClinicalTrialState {
    public static final int UPDATE_CLINIC = 2;
    private Clinic clinic;

    public UpdateClinicState(ClinicalTrialStateMachine machine, Context context, Clinic clinic) {
        super(machine, context);

        Activity act = getFromActivity();
        this.clinic = clinic;

        if (clinic != null && act != null) {
            Intent intent = new Intent(act, ClinicActivity.class);
            intent.putExtra(context.getResources().getString(R.string.intent_update_clinic), clinic);
            act.startActivityForResult(intent, UPDATE_CLINIC);
        }
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_OK:
                Activity current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(
                            getContext().getResources().getString(R.string.intent_updated_or_added),
                            clinic);
                    getCurrentActivity().setResult(RESULT_OK, returnIntent);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    getCurrentActivity().setResult(RESULT_OK);
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

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    @Override
    public boolean hasReadings() {
        boolean answer = false;
        try {
            answer = getMachine().getApplication().getModel().hasReadings(clinic);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }

        return answer;
    }
}

