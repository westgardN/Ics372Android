package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

/**
 * @author That GRoup
 */
public abstract class PatientState extends ClinicalTrialState {
    public static final int ADD_PATIENT = 1;
    public static final int UPDATE_PATIENT = 2;
    protected Patient patient;

    /**
     *
     * @param machine
     * @param context
     */
    public PatientState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    /**
     *
     * @return
     */
    public Patient getPatient() {
        return patient;
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
    public boolean canEndTrial() { return false; }

    /**
     *
     * @return
     */
    public boolean canStartTrial() { return false; }
}
