package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicsactivity.ClinicsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

/**
 * @author That GRoup
 */
public class ClinicsState extends ClinicalTrialState {
    private Clinic clinic;

    /**
     *
     * @param machine
     * @param context
     */
    public ClinicsState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();
        Intent intent = new Intent(act, ClinicsActivity.class);
        act.startActivity(intent);
    }

    /**
     *
     * @param clinic
     */
    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    /**
     *
     * @return
     */
    @Override
    public Clinic getClinic() {
        return clinic;
    }

    /**
     *
     * @param event
     */
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

    /**
     *
     */
    @Override
    public void onCleanup() {

    }

    /**
     *
     */
    @Override
    public void onSave() {

    }

    /**
     *
     */
    @Override
    public void onReturn() {

    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasClinic() {
        return clinic != null;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean canAdd() {
        return true;
    }
}

