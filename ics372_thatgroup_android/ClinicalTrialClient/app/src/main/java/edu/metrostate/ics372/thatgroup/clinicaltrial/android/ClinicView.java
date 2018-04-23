package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

public interface ClinicView extends BaseView<ClinicPresenter>{

    void setClinicId(String id);

    void setClinicName(String name);

    String getClinicId();

    String getClinicName();

    void setVisibleAddReading(boolean visible);

    void setVisibleViewReadings(boolean visible);

    void setVisibleSave(boolean visible);

    void setDisabledSave(boolean visible);

    void setDisabledId(boolean visible);

    void setDisabledName(boolean visible);
}
