package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

/**
 * @author That Group
 */
public abstract class ClinicState extends ClinicalTrialState {
    public static final int ADD_CLINIC = 1;
    public static final int UPDATE_CLINIC = 2;
    protected Clinic clinic;

    /**
     *
     * @param machine
     * @param context
     */
    public ClinicState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    /**
     *
     * @return
     */
    public Clinic getClinic() {
        return clinic;
    }

    /**
     *
     * @param clinic
     */
    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

}
