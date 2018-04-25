package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicactivity.ClinicActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * @author That Group
 */
public class AddClinicState extends ClinicState {
    /**
     *
     * @param machine
     * @param context
     */
    public AddClinicState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);

        Activity act = getFromActivity();

        if (act != null) {
            Intent intent = new Intent(act, ClinicActivity.class);
            act.startActivityForResult(intent, ADD_CLINIC);
        }
    }

    /**
     *
     * @param event
     */
    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_ERROR:
                machine.transition(new ClinicErrorState(machine, getCurrentActivity()), true);
                break;
            case ON_OK:
                Activity current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(
                            getContext().getResources().getString(R.string.intent_updated_or_added),
                            clinic);
                    getCurrentActivity().setResult(RESULT_OK, returnIntent);
                    getCurrentActivity().finish();
                }
                machine.transition();
                break;
            case ON_CANCEL:
            case ON_PREVIOUS:
                current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    getCurrentActivity().setResult(RESULT_CANCELED);
                    getCurrentActivity().finish();
                }
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

    /**
     *
     * @return
     */
    @Override
    public boolean canUpdate() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean canAdd() {
        return true;
    }

}

