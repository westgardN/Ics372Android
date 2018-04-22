package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

public interface ClinicView extends BaseView<ClinicPresenter>{

    void setId(String id);

    void setName(String name);

    void setVisibleAddReading(boolean visible);

    void setVisibleViewReadings(boolean visible);

    void setVisibleSave(boolean visible);

    void setDisabledAddReading(boolean visible);

    void setDisabledViewReadings(boolean visible);

    void setDisabledSave(boolean visible);

    void setDisabledId(boolean visible);

    void setDisabledName(boolean visible);
}
