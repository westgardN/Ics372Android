package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BaseView;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicsactivity.ClinicsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;


/**
 * @author That Group
 */
public interface ClinicsView extends BaseView<ClinicsPresenter> {

    void setClinics(List<Clinic> clinics);
}
