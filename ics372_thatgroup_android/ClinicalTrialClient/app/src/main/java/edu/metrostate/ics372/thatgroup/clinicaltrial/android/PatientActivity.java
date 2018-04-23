package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientErrorState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;

public class PatientActivity extends AppCompatActivity implements PatientFragment.OnFragmentInteractionListener {
    private ClinicalTrialStateMachine machine;
    private PatientPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState current = (ClinicalTrialState) machine.getCurrentState();
        current.setCurrentActivity(this);
        presenter = new PatientPresenter(machine);

        Intent intent = getIntent();
        Patient patient = null;

        if (intent.hasExtra(getResources().getString(R.string.intent_update_patient))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_patient));

            if (obj instanceof Patient) {
                patient = (Patient) obj;
            }
        }

        if (patient == null) {
            patient = new Patient();
        }

        presenter.setPatient(patient);
        PatientFragment fragment = PatientFragment.newInstance(patient);
        fragment.setPresenter(presenter);

        getFragmentManager().beginTransaction().add(R.id.fragment_patient, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }

    @Override
    public void onSaveClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    @Override
    public void onStartTrialClicked() {

    }

    @Override
    public void onEndTrialClicked() {

    }

    @Override
    public void onViewReadingsClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_VIEW_READINGS);
    }

    @Override
    public void onAddReadingClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_ADD_READING);
    }

    @Override
    public void onInputError() {
        machine.process(ClinicalTrialEvent.ON_ERROR);
        presenter.updateView(false);
    }

    @Override
    public void onInputOk() {
        if (machine.getCurrentState() instanceof PatientErrorState) {
            machine.process(ClinicalTrialEvent.ON_OK);
        }
        presenter.updateView(false);
    }
}
