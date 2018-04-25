package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

/**
 * @author That Group
 */
public interface State {
    String getName();

    void process(ClinicalTrialEvent event);

    void onCleanup();

    void onSave();

    void onReturn();
}
