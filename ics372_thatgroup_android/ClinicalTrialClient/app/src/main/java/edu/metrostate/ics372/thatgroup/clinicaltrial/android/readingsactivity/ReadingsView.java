package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BaseView;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientsactivity.PatientsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public interface ReadingsView extends BaseView<ReadingsPresenter> {
    void setReadings(List<Reading> readings);

    void setVisibleAddReading(boolean visible);
}
