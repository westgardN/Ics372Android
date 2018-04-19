package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ClinicalTrialStateMachine {
    Deque<ClinicalTrialState> previousStates;
    ClinicalTrialState currentState;
    List<ClinicalTrialChangedEventListener> listeners;

    public ClinicalTrialStateMachine() {
        currentState = null;
        previousStates = new LinkedList<>();
        listeners = new LinkedList<>();
    }

    public void addListener(ClinicalTrialChangedEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ClinicalTrialChangedEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyChange() {
        for (ClinicalTrialChangedEventListener listener : listeners) {
            listener.clinicalTrialChanged();
        }
    }
}
