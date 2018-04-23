package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;

public interface ClinicsView extends BaseView<ClinicsPresenter>{

    void setClinics(List<Clinic> clinics);
}
