package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

public class ExportingState extends ClinicalTrialState {
    public ExportingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Log.d(getClass().getName(), getFromActivity().getResources().getString(R.string.export_started));
        Toast.makeText(
                getFromActivity().getApplicationContext(),
                getFromActivity().getResources().getString(R.string.export_started),
                Toast.LENGTH_SHORT).show();
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
            case ON_EXPORT_END:
                String msg = message != null ? message :
                        getCurrentActivity().getResources().getString(R.string.export_complete);
                Log.d(getClass().getName(), msg);
                Toast.makeText(
                        getCurrentActivity().getApplicationContext(),
                        msg,
                        Toast.LENGTH_SHORT).show();
                getCurrentActivity().finish();
                machine.transition();
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                Log.d(getClass().getName(), getFromActivity().getResources().getString(R.string.export_cancelled));
                Toast.makeText(
                        getFromActivity().getApplicationContext(),
                        getFromActivity().getResources().getString(R.string.export_cancelled),
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
