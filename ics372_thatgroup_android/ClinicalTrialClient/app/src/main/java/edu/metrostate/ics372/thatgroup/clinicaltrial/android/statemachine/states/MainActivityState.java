package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states;

import android.content.Context;



import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;

/**
 * @author That Group
 */
public class MainActivityState extends ClinicalTrialState {
    /**
     *
     * @param machine
     * @param context
     */
    public MainActivityState(ClinicalTrialStateMachine machine, Context context) {
        super(machine, context);
    }

    /**
     *
     * @param event
     */
    @Override
    public void process(ClinicalTrialEvent event) {
        ClinicalTrialStateMachine machine = getMachine();

        switch (event) {
            case ON_IMPORT:
                machine.transition(new ImportState(machine, getContext()), true);
                break;
            case ON_EXPORT:
                machine.transition(new ExportState(machine, getContext()), true);
                break;
            case ON_CLINICS:
                machine.transition(new ClinicsState(machine, getContext()), true);
                break;
            case ON_PATIENTS:
                machine.transition(new PatientsState(machine, getContext()), true);
                break;
            case ON_READINGS:
                machine.transition(new ReadingsState(machine, getContext()), true);
                break;
            case ON_ADD_READING:
                machine.transition(new AddReadingState(machine, getContext()), true);
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
