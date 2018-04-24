package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

class ReadingErrorState extends ClinicalTrialState {
    public ReadingErrorState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
        ClinicalTrialClient app = machine.getApplication();
        Toast.makeText(
                app.getApplicationContext(),
                getFromActivity().getResources().getString(R.string.err_reading_fill_out_id),//TODO add more checks for other fields
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();
        ClinicalTrialClient app = machine.getApplication();

        switch (event) {
            case ON_OK:
                machine.transition();
                break;
            case ON_ERROR:
                Toast.makeText(
                        app.getApplicationContext(),
                        getFromActivity().getResources().getString(R.string.err_reading_fill_out_id),//TODO add more checks for other fields
                        Toast.LENGTH_SHORT).show();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                machine.transition();
                machine.process(event);
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
}