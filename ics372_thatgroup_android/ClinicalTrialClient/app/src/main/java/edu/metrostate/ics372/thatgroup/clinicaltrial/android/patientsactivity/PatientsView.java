package edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BaseView;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientsactivity.PatientsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

public interface PatientsView extends BaseView<PatientsPresenter> {

    void setPatients(List<Patient> patients);
}
