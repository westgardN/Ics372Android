package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientsactivity.PatientsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

/**
 * @author That Group
 */
public class PatientsState extends ClinicalTrialState {
    private Patient patient;

    /**
     *
     * @param machine
     * @param context
     */
    public PatientsState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();
        Intent intent = new Intent(act, PatientsActivity.class);
        act.startActivity(intent);
    }

    /**
     *
     * @param patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     *
     * @return
     */
    @Override
    public Patient getPatient() {
        return patient;
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
                if (hasPatient()) {
                    machine.transition(new UpdatePatientState(machine, getCurrentActivity(), getPatient()), true);
                }
                break;
            case ON_ADD:
                if (canAdd()) {
                    machine.transition(new AddPatientState(machine, getCurrentActivity()), true);
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
    public boolean hasPatient() {
        return patient != null;
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

