package edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientactivity;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

/**
 * @author That Group
 */
public class PatientPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private PatientView view = null;

    private Patient patient = null;

    /**
     *
     * @param machine
     */
    public PatientPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    /**
     *
     * @param view
     */
    public void setView(PatientView view){ this.view = view; }

    /**
     *
     * @param patient
     */
    public void setPatient(Patient patient) { this.patient = patient; }

    /**
     *
     * @return
     */
    public Patient getPatient() {
        if (view != null) {
            if (patient == null) {
                patient = new Patient();
            }

            patient.setId(view.getPatientId());
            patient.setTrialStartDate(view.getStartDate());
            patient.setTrialEndDate(view.getEndDate());
            patient.setStatusId(view.getStatusId());
            patient.setTrialId(machine.getApplication().getModel().getTrialId());
        }

        return patient;
    }

    /**
     *
     */
    @Override
    public void subscribe(){ updateView(); }

    /**
     *
     */
    @Override
    public void unsubscribe(){ }

    /**
     *
     */
    public void  updateView() {
        updateView(true);
    }

    /**
     *
     * @param setData
     */
    public void  updateView(boolean setData) {
        if (view != null && patient != null) {
            ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

            if (state instanceof PatientState) {
                PatientState pState = (PatientState)state;
                if (setData) {
                    view.setStartDate(patient.getTrialStartDate());
                    view.setEndDate(patient.getTrialEndDate());
                    view.setStatusId(patient.getStatusId());
                    view.setDisabledId(!state.canAdd());
                    view.setDisabledStartTrial(!pState.canStartTrial());
                    view.setDisabledEndTrial(!pState.canEndTrial());
                    view.setVisibleStartTrial(!state.canAdd());
                    view.setVisibleEndTrial(!state.canAdd());
                    view.setVisibleAddReading(state.canAddReading());
                    view.setVisibleViewReadings(state.canViewReadings());
//                    view.setVisibleSave(state.canAdd());
                    view.setDisabledSave(true);
                    view.setPatientId(patient.getId());
                } else {
                    view.setDisabledSave(!(state.canUpdate() || state.canAdd()));
                    view.setDisabledStartTrial(!pState.canStartTrial());
                    view.setDisabledEndTrial(!pState.canEndTrial());
                }
            }
        }
    }
}
