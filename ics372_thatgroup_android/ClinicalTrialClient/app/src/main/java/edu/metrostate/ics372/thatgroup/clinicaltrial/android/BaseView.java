package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

/**
 * @author That Group
 * @param <T>
 */
public interface BaseView <T extends BasePresenter> {

    void setPresenter(T presenter);
}
