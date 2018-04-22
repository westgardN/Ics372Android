package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

public interface BasePresenter {
    void subscribe();
    void unsubscribe();
}