package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientactivity.PatientActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.PatientStatus;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class UpdatePatientState extends PatientState {

    public UpdatePatientState(ClinicalTrialStateMachine machine, Context context, Patient patient) {
        super(machine, context);

        Activity act = getFromActivity();
        this.patient = patient;

        if (patient != null && act != null) {
            Intent intent = new Intent(act, PatientActivity.class);
            intent.putExtra(context.getResources().getString(R.string.intent_update_patient), patient);
            act.startActivityForResult(intent, UPDATE_PATIENT);
        }
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_ERROR:
                machine.transition(new PatientErrorState(machine, getCurrentActivity()), true);
                break;
            case ON_OK:
                Activity current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(
                            getContext().getResources().getString(R.string.intent_updated_or_added),
                            patient);
                    getCurrentActivity().setResult(RESULT_OK, returnIntent);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    getCurrentActivity().setResult(RESULT_CANCELED);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
            case ON_VIEW_READINGS:
                current = getCurrentActivity();

                machine.transition(new ReadingsState(machine, current, patient), true);
                break;
            case ON_ADD_READING:
                if (canAddReading()) {
                    machine.transition(new AddReadingState(machine, getCurrentActivity(), patient), true);
                }
                break;
        }
    }

    @Override
    public void onCleanup() {

    }

    @Override
    public void onSave() {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean canAddReading() {
        boolean answer = false;

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

    @Override
    public boolean canViewReadings() {
        return hasReadings();
    }

    @Override
    public boolean hasReadings() {
        boolean answer = false;

        try {
            answer = getMachine().getApplication().getModel().hasReadings(patient);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    public boolean canEndTrial() {
        boolean answer = false;

        if (patient != null && Objects.equals(patient.getStatusId(), PatientStatus.ACTIVE_ID)) {
            answer = true;
        }

        return answer;
    }

    @Override
    public boolean canStartTrial() {
        boolean answer = false;

        if (patient != null && !Objects.equals(patient.getStatusId(), PatientStatus.ACTIVE_ID)) {
            answer = true;
        }

        return answer;
    }
}

