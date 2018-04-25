package edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientactivity;

import java.time.LocalDate;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BaseView;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientactivity.PatientPresenter;

/**
 * @author That Group
 */

public interface PatientView extends BaseView<PatientPresenter> {

    void setPatientId(String id);

    void setStartDate(LocalDate date);

    void setEndDate(LocalDate date);

    void setStatusId(String statusId);

    String getPatientId();

    LocalDate getStartDate();

    LocalDate getEndDate();

    String getStatusId();

    void setVisibleStartTrial(boolean visible);

    void setVisibleEndTrial(boolean visible);

    void setVisibleAddReading(boolean visible);

    void setVisibleViewReadings(boolean visible);

    void setVisibleSave(boolean visible);

    void setDisabledSave(boolean disable);

    void setDisabledId(boolean disable);

    void setDisabledStartTrial(boolean disable);

    void setDisabledEndTrial(boolean disable);
}
