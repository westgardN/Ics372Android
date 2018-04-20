package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

public interface State {
    String getName();

    void process(ClinicalTrialEvent event);

    void onCleanup();

    void onPrevious();
}
