package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BaseView;

public interface ReadingView extends BaseView<ReadingPresenter> {
    String getReadingId();
    String getClinicId();
    String getPatientId();
    String getType();
    String getValue();
    LocalDate getDate();
    LocalTime getTime();

    void setReadingId(String id);
    void setClinicId(String clinicId);
    void setPatientId(String patientId);
    void setType(String type);
    void setValue(String value);
    void setDate(LocalDate date);
    void setTime(LocalTime time);

    void setDateAndTimePickerListeners(View view);
    void setDisabledSave(boolean b);
}
