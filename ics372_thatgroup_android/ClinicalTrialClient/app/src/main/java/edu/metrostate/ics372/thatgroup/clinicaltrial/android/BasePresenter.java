package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

/**
 * @author That Group
 */
public interface BasePresenter {
    void subscribe();
    void unsubscribe();
}