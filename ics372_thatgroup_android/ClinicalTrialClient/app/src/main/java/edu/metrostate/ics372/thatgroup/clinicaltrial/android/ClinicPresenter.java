package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public class ClinicPresenter implements BasePresenter{

    private ClinicView view = null;

    private Clinic clinic = null;

    public void setView(ClinicView view){ this.view = view; }

    public void setClinic(Clinic clinic) { this.clinic = clinic; }

    @Override
    public void subscribe(){ updateView(); }

    @Override
    public void unsubscribe(){ }

    public void  updateView() {
        if (view != null && clinic != null) {
            view.setId(clinic.getId());
            view.setName(clinic.getName());
        }
    }
}
