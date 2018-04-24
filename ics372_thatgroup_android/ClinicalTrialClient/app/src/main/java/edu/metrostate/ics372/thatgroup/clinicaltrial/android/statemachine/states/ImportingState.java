package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

public class ImportingState extends ClinicalTrialState {
    public ImportingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_SELECT:
                break;
            case ON_IMPORT_END:
                String msg = message != null ? message :
                        getFromActivity().getResources().getString(R.string.import_complete);
                Toast.makeText(
                        getFromActivity().getApplicationContext(),
                        msg,
                        Toast.LENGTH_SHORT).show();
                getCurrentActivity().finish();
                machine.transition();
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                Log.d(getClass().getName(), "Import cancelled");
                Toast.makeText(
                        getFromActivity().getApplicationContext(),
                        getFromActivity().getResources().getString(R.string.import_cancelled),
                        Toast.LENGTH_SHORT).show();
                getCurrentActivity().finish();
                machine.transition();
                machine.transition();
                break;
            default:
                super.process(event);
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