package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;


/**
 * @author That Group
 */
public class ClinicsPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private ClinicsView view = null;

    private List<Clinic> clinics = null;

    public void setView(ClinicsView view){ this.view = view; }

    public void setClinics(List<Clinic> clinics) { this.clinics = clinics; }

    public void addClinic(Clinic clinic) {
        if (clinics != null) {
            clinics.add(clinic);
        }
    }

    /**
     *
     * @param clinic
     */
    public void updateClinic(Clinic clinic) {
        if (clinics != null && clinics.contains(clinic)) {
            clinics.remove(clinic);

            clinics.add(clinic);
        }
    }

    /**
     *
     */
    @Override
    public void subscribe(){
        updateView();
    }

    /**
     *
     */
    @Override
    public void unsubscribe(){ }

    /**
     *
     */
    public void  updateView() {
        if (view != null && clinics != null) {
            view.setClinics(clinics);
        }
    }
}
