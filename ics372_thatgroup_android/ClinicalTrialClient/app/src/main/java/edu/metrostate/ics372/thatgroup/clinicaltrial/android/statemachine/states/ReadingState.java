package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

/**
 * @author That Group
 */
public abstract class ReadingState extends ClinicalTrialState {
    public static final int ADD_READING = 1;
    public static final int UPDATE_READING = 2;
    protected Reading reading;
    protected Object obj;

    /**
     *
     * @param machine
     * @param context
     */
    public ReadingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    /**
     *
     * @return
     */
    public Reading getReading() {
        return reading;
    }

    /**
     *
     * @param reading
     */
    public void setReading(Reading reading) {
        this.reading = reading;
    }

    /**
     *
     * @param obj
     */
    public void setObject(Object obj) {
        this.obj = obj;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasClinic() {
        return getClinic() != null;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasPatient() {
        return getPatient() != null;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasReading() {
        return getReading() != null;
    }

    /**
     *
     * @return
     */
    @Override
    public Clinic getClinic() {
        Clinic answer = null;

        if (obj instanceof Clinic) {
            answer = (Clinic) obj;
        }

        return answer;
    }

    /**
     *
     * @return
     */
    @Override
    public Patient getPatient() {
        Patient answer = null;

        if (obj instanceof Patient) {
            answer = (Patient) obj;
        }

        return answer;
    }

}
