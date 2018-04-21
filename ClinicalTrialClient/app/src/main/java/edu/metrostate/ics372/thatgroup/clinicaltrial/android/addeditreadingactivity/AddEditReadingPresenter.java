package edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class AddEditReadingPresenter {
    private Clinic clinic;
    private Patient patient;
    private ClinicalTrialModel model;


    public AddEditReadingPresenter(Object bean, ClinicalTrialModel model) {
        if (bean instanceof Clinic) {
            clinic = (Clinic) bean;
            try {
                model.setSelectedClinic(clinic);
            } catch (TrialCatalogException e) {
                e.printStackTrace();
            }
        }

        if (bean instanceof Patient) {
            patient = (Patient) bean;
            try {
                model.setSelectedPatient(patient);
            } catch (TrialCatalogException e) {
                e.printStackTrace();
            }
        }
        this.model = model;
    }

    public String getClinicId() {
        return clinic.getId();
    }

    public String getPatientId() {
        return patient.getId();
    }

    public boolean addNewReading(String[] readingProperties) {
        boolean answer = false;
        if (validated(readingProperties)) {
            Reading reading = buildReading(readingProperties);
            try {
                if (model.addReading(reading)) {
                    answer = true;
                }
            } catch (TrialCatalogException e) {
                e.printStackTrace();
            }
        }
        System.out.println(answer);
        return answer;
    }

    public boolean editReading(Reading reading) {
        boolean answer = false;
        try {
            if (model.updateOrAdd(reading)) {
                answer = true;
            }
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        return answer;
    }

    private boolean validated(String[] readingProperties) {
        //TODO implement validation for the reading form
        return true;
    }

    private Reading buildReading(String[] readingProperties) {
        String id = readingProperties[0];
        String clinicId = readingProperties[1];
        String patientId = readingProperties[2];
        String type = readingProperties[3];
        String value = readingProperties[4];
        String date = readingProperties[5];
        String time = readingProperties[6];
        Reading reading = ReadingFactory.getReading(type);

        reading.setId(id);
        reading.setClinicId(clinicId);
        reading.setPatientId(patientId);
        reading.setValue(value);
        reading.setDate(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)));
        return reading;
    }
}
