package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public abstract class ClinicState extends ClinicalTrialState {
    public static final int ADD_CLINIC = 1;
    public static final int UPDATE_CLINIC = 2;
    protected Clinic clinic;

    public ClinicState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

}
