package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public class ClinicPresenter implements BasePresenter{
    private ClinicalTrialStateMachine machine;

    private ClinicView view = null;

    private Clinic clinic = null;

    public ClinicPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    public void setView(ClinicView view){ this.view = view; }

    public void setClinic(Clinic clinic) { this.clinic = clinic; }

    public Clinic getClinic() { return clinic; }

    @Override
    public void subscribe(){ updateView(); }

    @Override
    public void unsubscribe(){ }

    public void  updateView() {
        if (view != null && clinic != null) {
            ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

            view.setId(clinic.getId());
            view.setName(clinic.getName());

            view.setDisabledId(state.canAdd());
            view.setDisabledName((state.canUpdate() || state.canAdd()));

            view.setVisibleAddReading(state.canAddReading());
            view.setVisibleViewReadings(state.canViewReadings());
            view.setVisibleSave((state.canUpdate() || state.canAdd()));
        }
    }
}
