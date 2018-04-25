package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ApplicationStartState;

/**
 * @author That Group
 */
public class ClinicalTrialStateMachine {
    Deque<State> previousStates;
    State currentState;
    List<ClinicalTrialChangedEventListener> listeners;
    ClinicalTrialClient application;

    /**
     *
     * @return
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     *
     * @return
     */
    public ClinicalTrialClient getApplication() {
        return application;
    }

    /**
     *
     * @param app
     */
    public ClinicalTrialStateMachine(ClinicalTrialClient app) {
        application = app;
        currentState = new ApplicationStartState(this, app);

        previousStates = new LinkedList<>();
        listeners = new LinkedList<>();
    }

    /**
     *
     * @param listener
     */
    public void addListener(ClinicalTrialChangedEventListener listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeListener(ClinicalTrialChangedEventListener listener) {
        listeners.remove(listener);
    }

    /**
     *
     */
    private void notifyChange() {
        for (ClinicalTrialChangedEventListener listener : listeners) {
            listener.clinicalTrialChanged();
        }
    }

    /**
     *
     * @param event
     */
    public void process(ClinicalTrialEvent event) {
        Log.i(getClass().getSimpleName(),"Received Event: " + event.toString());
        currentState.process(event);
        notifyChange();
    }

    /**
     *
     * @param destinationState
     */
    public void transition(ClinicalTrialState destinationState) {
        transition(destinationState, false);
    }

    /**
     *
     * @param destinationState
     * @param returnable
     */
    public void transition(ClinicalTrialState destinationState, boolean returnable) {
        if (returnable) {
            Log.i(getClass().getSimpleName(),"Saving state: " + currentState.getName());
            currentState.onSave();
            previousStates.push(currentState);
        }

        Log.i(getClass().getSimpleName(),"Leaving state: " + currentState.getName());

        currentState.onCleanup();

        currentState = destinationState;
        notifyChange();
        Log.i(getClass().getSimpleName(),"Entering state: " + currentState.getName());
    }

    /**
     *
     */
    public void transition() {
        if (!hasPrevious()) {
            Log.i(getClass().getSimpleName(),"No previous state to transition to.");
            return;
        }

        Log.i(getClass().getSimpleName(),"Leaving state: " + currentState.getName());
        currentState.onCleanup();
        currentState = previousStates.pop();
        currentState.onReturn();
        Log.i(getClass().getSimpleName(),"Returning to state: " + currentState.getName());
        notifyChange();

    }

    /**
     *
     * @return
     */
    public boolean hasPrevious() {
        return !previousStates.isEmpty();
    }

    /**
     *
     */
    public void clearPreviousStates() { previousStates.clear(); }
}
