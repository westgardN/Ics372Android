package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.app.Activity;
import android.content.Context;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ImportActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.MainActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

public class MainActivityState extends ClinicalTrialState {

    public MainActivityState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    @Override
    public String getName() {
        String answer = "";

        Activity act = getActivity();

        if (act != null) {
            answer = act.getLocalClassName();
        } else {
            getMachine().getApplication().getClass().getSimpleName();
        }

        return answer;
    }

    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_IMPORT:
                machine.transition(new ImportActivityState(machine, getContext()), true);
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
