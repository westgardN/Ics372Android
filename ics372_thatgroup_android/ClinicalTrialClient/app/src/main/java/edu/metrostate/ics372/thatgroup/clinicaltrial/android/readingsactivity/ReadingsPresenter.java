package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import java.util.List;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

/**
 * @author That Group
 */
public class ReadingsPresenter implements BasePresenter {
    ClinicalTrialStateMachine machine;

    /**
     *
     * @param machine
     */
    public ReadingsPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    private ReadingsView view = null;

    private List<Reading> readings = null;

    /**
     *
     * @param view
     */
    public void setView(ReadingsView view){ this.view = view; }

    /**
     *
     * @param readings
     */
    public void setReadings(List<Reading> readings) { this.readings = readings; }

    /**
     *
     * @param reading
     */
    public void addReading(Reading reading) {
        if (readings != null) {
            readings.add(reading);
        }
    }

    /**
     *
     * @param reading
     */
    public void updateReading(Reading reading) {
        if (readings != null && readings.contains(reading)) {
            readings.remove(reading);
            readings.add(reading);
        }
    }

    /**
     *
     */
    @Override
    public void subscribe(){
        updateView();
    }

    /**
     *
     */
    @Override
    public void unsubscribe(){ }

    /**
     *
     */
    public void  updateView() {
        ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

        if (view != null && readings != null) {
            view.setReadings(readings);

            view.setVisibleAddReading(state.canAdd());

        }
    }
}
