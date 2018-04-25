package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

public abstract class PatientState extends ClinicalTrialState {
    public static final int ADD_PATIENT = 1;
    public static final int UPDATE_PATIENT = 2;
    protected Patient patient;

    public PatientState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean canEndTrial() { return false; }

    public boolean canStartTrial() { return false; }
}
