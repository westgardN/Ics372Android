package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ReadingsPresenter {
    ClinicalTrialModel model;
    private List<Reading> readings;
    private Reading activeReading;

    public ReadingsPresenter(ClinicalTrialModel model) throws TrialCatalogException {
        this.model = model;
        readings = model.getReadings();
    }

    public ReadingsPresenter(ClinicalTrialModel model, Clinic clinic) throws TrialCatalogException {
        this.model = model;
        readings = model.getJournal(clinic);
    }

    public ReadingsPresenter(ClinicalTrialModel model, Patient patient) throws TrialCatalogException {
        this.model = model;
        readings = model.getJournal(patient);
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public Reading getActiveReading() {
        return activeReading;
    }

    public void setActiveReading(Reading activeReading) {
        this.activeReading = activeReading;
    }

    public String getReadingId(Reading reading) {
        return reading.getId();
    }

    public String getReadingType(Reading reading) {
        return ReadingFactory.getPrettyReadingType(reading);
    }
}
