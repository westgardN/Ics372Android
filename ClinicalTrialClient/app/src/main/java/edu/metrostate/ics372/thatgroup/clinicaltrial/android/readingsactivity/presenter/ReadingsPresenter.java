package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ReadingsPresenter {
    List<Reading> readings;

    public ReadingsPresenter(ClinicalTrialModel model) throws TrialCatalogException {
        readings = model.getReadings();
    }

    public ReadingsPresenter(ClinicalTrialModel model, Clinic clinic) throws TrialCatalogException {
        readings = model.getJournal(clinic);
    }

    public ReadingsPresenter(ClinicalTrialModel model, Patient patient) throws TrialCatalogException {
        readings = model.getJournal(patient);
    }

    public List<Reading> getReadings() {
        return readings;
    }
}
