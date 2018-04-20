package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;

public class ClinicalTrialStateMachine {
    Deque<ClinicalTrialState> previousStates;
    ClinicalTrialState currentState;
    List<ClinicalTrialChangedEventListener> listeners;
    ClinicalTrialClient application;

    public ClinicalTrialState getCurrentState() {
        return currentState;
    }

    public ClinicalTrialClient getApplication() {
        return application;
    }

    public ClinicalTrialStateMachine(ClinicalTrialClient app) {
        application = app;
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

    public void process(ClinicalTrialEvent event) {
        Log.i(getClass().getSimpleName().toString(),"Received Event: " + event.toString());
        currentState.process(event);
        notifyChange();
    }

    public void transition(ClinicalTrialState destinationState) {
        transition(destinationState, false);
    }

    public void transition(ClinicalTrialState destinationState, boolean returnable) {
        if (returnable) {
            Log.i(getClass().getSimpleName().toString(),"Saving state: " + currentState.toString());
            currentState.onSave();
            previousStates.push(currentState);
        }
        Log.i(getClass().getSimpleName().toString(),"Leaving state: " + currentState.toString());
        currentState.onCleanup();
        currentState = destinationState;
        notifyChange();
        Log.i(getClass().getSimpleName().toString(),"Entering state: " + currentState.toString());
    }

    public void transition() {
        if (!hasPrevious()) {
            Log.i(getClass().getSimpleName().toString(),"No previous state to transition to.");
            return;
        }

        Log.i(getClass().getSimpleName().toString(),"Leaving state: " + currentState.toString());
        currentState.onCleanup();
        currentState = previousStates.pop();
        currentState.onReturn();
        notifyChange();
        Log.i(getClass().getSimpleName().toString(),"Entering state: " + currentState.toString());

    }

    public boolean hasPrevious() {
        return !previousStates.isEmpty();
    }
}
