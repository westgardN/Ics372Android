package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.ReadingsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.PatientStatus;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

/**
 * @author That Group
 */
public class ReadingsState  extends ClinicalTrialState {
    private Object obj;

    /**
     *
     * @param machine
     * @param context
     */
    public ReadingsState(ClinicalTrialStateMachine machine, Context context) {
        this(machine, context, null);
    }

    /**
     *
     * @param machine
     * @param context
     * @param obj
     */
    public ReadingsState(ClinicalTrialStateMachine machine, Context context, Object obj) {
        super(machine, context);
        setObject(obj);


        Activity act = getFromActivity();
        Intent intent = new Intent(act, ReadingsActivity.class);

        if (hasClinic()) {
            intent.putExtra(context.getResources().getString(R.string.intent_view_readings_clinic),
                    getClinic());
        } else if (hasPatient()) {
            intent.putExtra(context.getResources().getString(R.string.intent_view_readings_patient),
                    getPatient());
        }

        act.startActivity(intent);
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
    public Reading getReading() {
        Reading answer = null;

        if (obj instanceof Reading) {
            answer = (Reading) obj;
        }

        return answer;
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
                if (hasReading()) {
                    machine.transition(new UpdateReadingState(machine, getCurrentActivity(), getReading()), true);
                }
                break;
            case ON_ADD:
                if (canAdd()) {
                    if (hasClinic()) {
                        machine.transition(new AddReadingState(machine, getCurrentActivity(), getClinic()), true);
                    } else if (hasPatient()) {
                        machine.transition(new AddReadingState(machine, getCurrentActivity(), getPatient()), true);
                    } else {
                        machine.transition(new AddReadingState(machine, getCurrentActivity()), true);
                    }
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
    public boolean canAdd() {
        boolean answer = true;

        if (hasPatient()) {
            answer = canAddReading();
        }
        return answer;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean canAddReading() {
        boolean answer = false;
        Patient patient = null;

        if (hasPatient()) {
            patient = getPatient();
        }

        if (patient != null) {
            Patient temp = null;
            try {
                temp = getMachine().getApplication().getModel().getPatient(patient.getId());
            } catch (TrialCatalogException e) {
                e.printStackTrace();
            }

            answer = Objects.equals(temp.getStatusId(), PatientStatus.ACTIVE_ID);
        }

        return answer;
    }

}
