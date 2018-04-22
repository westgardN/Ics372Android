package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

public interface BaseView <T extends BasePresenter> {

    void setPresenter(T presenter);
}
