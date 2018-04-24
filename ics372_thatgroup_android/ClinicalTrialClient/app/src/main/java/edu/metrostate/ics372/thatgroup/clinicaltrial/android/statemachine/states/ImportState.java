package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ImportActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

class ImportState extends ClinicalTrialState {
    public ImportState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();
        Intent intent = new Intent(act, ImportActivity.class);
        act.startActivity(intent);
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_SELECT:
                break;
            case ON_IMPORT_BEGIN:
                ImportingState state = new ImportingState(machine, getCurrentActivity());
                state.setCurrentActivity(getCurrentActivity());
                machine.transition(state, true);
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                Log.d(getClass().getName(), getFromActivity().getResources().getString(R.string.import_cancelled));
                Toast.makeText(
                        getFromActivity().getApplicationContext(),
                        getFromActivity().getResources().getString(R.string.import_cancelled),
                        Toast.LENGTH_SHORT).show();
                getCurrentActivity().finish();
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
