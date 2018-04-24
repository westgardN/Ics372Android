package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public abstract class ReadingState extends ClinicalTrialState {
    public static final int ADD_READING = 1;
    public static final int UPDATE_READING = 2;
    protected Reading reading;

    public ReadingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }
}
