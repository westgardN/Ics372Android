package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

public abstract class ClinicalTrialState implements State {
    private ClinicalTrialStateMachine machine;

    public ClinicalTrialState(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    @Override
    public String toString() {
        return this.getClass().toString();
    }

    public ClinicalTrialStateMachine getMachine() {
        return machine;
    }
}
