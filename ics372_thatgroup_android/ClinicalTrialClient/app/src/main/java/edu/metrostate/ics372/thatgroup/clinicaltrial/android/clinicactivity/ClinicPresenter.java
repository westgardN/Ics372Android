package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicactivity;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

/**
 * @author That Group
 */

public class ClinicPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private ClinicView view = null;

    private Clinic clinic = null;

    public ClinicPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    public void setView(ClinicView view){ this.view = view; }

    public void setClinic(Clinic clinic) { this.clinic = clinic; }

    public Clinic getClinic() {
        if (view != null) {
            if (clinic == null) {
                clinic = new Clinic();
            }

            clinic.setId(view.getClinicId());
            clinic.setName(view.getClinicName());
            clinic.setTrialId(machine.getApplication().getModel().getTrialId());
        }

        return clinic;
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
        if (view != null && clinic != null) {
            ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

            if (setData) {
                view.setClinicName(clinic.getName());
                view.setDisabledId(!state.canAdd());
                view.setDisabledName(!(state.canUpdate() || state.canAdd()));
                view.setVisibleAddReading(state.canAddReading());
                view.setVisibleViewReadings(state.canViewReadings());
                view.setVisibleSave((state.canUpdate() || state.canAdd()));
                view.setDisabledSave(true);
                view.setClinicId(clinic.getId());
            } else {
                view.setDisabledSave(!(state.canUpdate() || state.canAdd()));
            }
        }
    }
}
