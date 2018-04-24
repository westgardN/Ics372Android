package edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

public class PatientsPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private PatientsView view = null;

    private List<Patient> patients = null;

    public void setView(PatientsView view){ this.view = view; }

    public void setPatients(List<Patient> patients) { this.patients = patients; }

    public void addPatient(Patient patient) {
        if (patients != null) {
            patients.add(patient);
        }
    }

    public void updatePatient(Patient patient) {
        if (patients != null && patients.contains(patient)) {
            patients.remove(patient);

            patients.add(patient);
        }
    }

    @Override
    public void subscribe(){
        updateView();
    }

    @Override
    public void unsubscribe(){ }

    public void  updateView() {
        if (view != null && patients != null) {
            view.setPatients(patients);
        }
    }
}
