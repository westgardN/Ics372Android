package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

public interface PatientsView extends BaseView<PatientsPresenter>{

    void setPatients(List<Patient> patients);
}
