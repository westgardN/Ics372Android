package edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class AddEditReadingPresenter {
    private Clinic clinic;
    private Patient patient;
    ClinicalTrialModel model;

    public AddEditReadingPresenter(Object bean, ClinicalTrialModel model) {
        if (bean instanceof Clinic) {
            clinic = (Clinic) bean;
        }

        if (bean instanceof Patient) {
            patient = (Patient) bean;
        }
        this.model = model;
    }

    public String getClinicId() {
        return clinic.getId();
    }

    public String getPatientId() {
        return patient.getId();
    }
}
