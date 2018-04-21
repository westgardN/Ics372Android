package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

public class ApplicationStartState extends ClinicalTrialState {
    public ApplicationStartState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void process(ClinicalTrialEvent event) {
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
