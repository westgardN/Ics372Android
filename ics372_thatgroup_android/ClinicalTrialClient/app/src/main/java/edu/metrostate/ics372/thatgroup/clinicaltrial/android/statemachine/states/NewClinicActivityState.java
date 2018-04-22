package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;


public class NewClinicActivityState extends ClinicalTrialState {
    Activity act;
    ClinicalTrialStateMachine machine;

    public NewClinicActivityState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
        this.machine = machine;
        Activity act = getActivity();
        Intent intent = new Intent(act, ClinicActivity.class);
        act.startActivity(intent);
    }

    @Override
    public void process(ClinicalTrialEvent event) {

        switch (event) {
            case ON_PREVIOUS:
                machine.transition();
                break;
            case ON_ERROR:

                break;
            case ON_OK:

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

