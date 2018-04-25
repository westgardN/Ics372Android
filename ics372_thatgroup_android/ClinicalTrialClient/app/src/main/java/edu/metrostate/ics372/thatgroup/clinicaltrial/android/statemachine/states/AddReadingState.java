package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicactivity.ClinicActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddReadingState extends ReadingState {

    public AddReadingState(ClinicalTrialStateMachine machine, Context context) {
        this(machine, context, null);
    }

    public AddReadingState(ClinicalTrialStateMachine machine, Context context, Object obj) {
        super(machine, context);
        setObject(obj);
        Activity act = getFromActivity();

        if (act != null) {
            Intent intent = new Intent(act, ReadingActivity.class);

            if (hasClinic()) {
                intent.putExtra(context.getResources().getString(R.string.intent_add_reading_clinic),
                        getClinic());
            } else if (hasPatient()) {
                intent.putExtra(context.getResources().getString(R.string.intent_add_reading_patient),
                        getPatient());
            }

            act.startActivityForResult(intent, ADD_READING);
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
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_ERROR:
                machine.transition(new ReadingErrorState(machine, getCurrentActivity()), true);
                break;
            case ON_OK:
                Activity current = getCurrentActivity();

                if (current != null && !current.isDestroyed()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(
                            getContext().getResources().getString(R.string.intent_updated_or_added),
                            reading);
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

    @Override
    public boolean canAdd() {
        return true;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
