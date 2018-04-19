package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

public interface State {
    String getName();

    void processEvent(ClinicalTrialEvent event);

    void onCleanup();
}
