package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public class ClinicsState extends ClinicalTrialState {
    private Clinic clinic;

    public ClinicsState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();
        Intent intent = new Intent(act, ClinicsActivity.class);
        act.startActivity(intent);
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    @Override
    public Clinic getClinic() {
        return clinic;
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_PREVIOUS:
                machine.transition();
                break;
            case ON_SELECT:
                if (hasClinic()) {
                    machine.transition(new UpdateClinicState(machine, getCurrentActivity(), getClinic()), true);
                }
                break;
            case ON_ADD:
                if (canAdd()) {
                    machine.transition(new AddClinicState(machine, getCurrentActivity()), true);
                }
                break;
            default:
                super.process(event);
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
    public boolean hasClinic() {
        return clinic != null;
    }

    @Override
    public boolean canAdd() {
        return true;
    }
}

