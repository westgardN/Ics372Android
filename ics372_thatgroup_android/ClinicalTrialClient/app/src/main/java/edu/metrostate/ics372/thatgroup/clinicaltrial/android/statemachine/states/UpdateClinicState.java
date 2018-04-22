package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public class UpdateClinicState extends ClinicalTrialState {
    static final int UPDATE_CLINIC = 2;
    public UpdateClinicState(ClinicalTrialStateMachine machine, Context context, Clinic clinic) {
        super(machine, context);

        Activity act = getActivity();

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
            case ON_PREVIOUS:
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
}

