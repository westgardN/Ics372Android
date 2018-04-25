package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

/**
 * @author That Group
 */
public class ImportingState extends ClinicalTrialState {
    /**
     *
     * @param machine
     * @param context
     */
    public ImportingState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Log.d(getClass().getName(), getFromActivity().getResources().getString(R.string.import_started));
    }

    private String message;

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /***
     *
     * @param event
     */
    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_SELECT:
                break;
            case ON_IMPORT_END:
                String msg = message != null ? message :
                        getFromActivity().getResources().getString(R.string.import_complete);
                Log.d(getClass().getName(), msg);
                Toast.makeText(
                        getFromActivity().getApplicationContext(),
                        msg,
                        Toast.LENGTH_LONG).show();
                getCurrentActivity().finish();
                machine.transition();
                machine.transition();
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
                machine.transition();
                break;
            default:
                super.process(event);
        }
    }

    /**
     *
     */
    @Override
    public void onCleanup() {

    }

    /**
     *
     */
    @Override
    public void onSave() {

    }

    /**
     *
     */
    @Override
    public void onReturn() {

    }
}
