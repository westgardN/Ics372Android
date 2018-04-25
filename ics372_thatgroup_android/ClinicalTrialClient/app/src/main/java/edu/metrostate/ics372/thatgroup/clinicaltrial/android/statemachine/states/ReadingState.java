package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public abstract class ReadingState extends ClinicalTrialState {
    public static final int ADD_READING = 1;
    public static final int UPDATE_READING = 2;
    protected Reading reading;
    protected Object obj;

    public ReadingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    @Override
    public boolean hasClinic() {
        return getClinic() != null;
    }

    @Override
    public boolean hasPatient() {
        return getPatient() != null;
    }

    @Override
    public boolean hasReading() {
        return getReading() != null;
    }

    @Override
    public Clinic getClinic() {
        Clinic answer = null;

        if (obj instanceof Clinic) {
            answer = (Clinic) obj;
        }

        return answer;
    }

    @Override
    public Patient getPatient() {
        Patient answer = null;

        if (obj instanceof Patient) {
            answer = (Patient) obj;
        }

        return answer;
    }

}
