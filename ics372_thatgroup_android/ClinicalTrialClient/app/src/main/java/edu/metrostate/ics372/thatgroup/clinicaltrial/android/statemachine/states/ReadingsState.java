package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.ReadingsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingsState  extends ClinicalTrialState {
    private Reading reading;

    public ReadingsState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();
        Intent intent = new Intent(act, ReadingsActivity.class);
        act.startActivity(intent);
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    @Override
    public Reading getReading() {
        return reading;
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_PREVIOUS:
                machine.transition();
                break;
            case ON_SELECT:
                if (hasReading()) {
                    machine.transition(new UpdateReadingState(machine, getCurrentActivity(), getReading()), true);
                }
                break;
            case ON_ADD:
                if (canAdd()) {
                    machine.transition(new AddReadingState(machine, getCurrentActivity()), true);
                }
                break;
            default:
                super.process(event);
                break;
        }
    }

    @Override
    public void onCleanup() {

    }

    @Override
    public void onSave() {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public boolean hasReading() {
        return reading != null;
    }

    @Override
    public boolean canAdd() {
        return true;
    }
}
