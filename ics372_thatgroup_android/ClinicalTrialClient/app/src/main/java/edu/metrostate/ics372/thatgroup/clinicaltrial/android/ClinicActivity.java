package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicActivity extends AppCompatActivity implements ClinicFragment.OnFragmentInteractionListener {

    private ClinicalTrialStateMachine machine;
    private ClinicPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState current = (ClinicalTrialState) machine.getCurrentState();
        current.setCurrentActivity(this);
        presenter = new ClinicPresenter(machine);

        ClinicalTrialModel model = machine.getApplication().getModel();

        setContentView(R.layout.activity_clinic);

        Intent intent = getIntent();
        Clinic clinic = null;

        if (intent.hasExtra(getResources().getString(R.string.intent_update_clinic))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_clinic));

            if (obj instanceof Clinic) {
                clinic = (Clinic) obj;
            }
        }

        if (clinic == null) {
            clinic = new Clinic();
        }

        presenter.setClinic(clinic);
        ClinicFragment fragment = ClinicFragment.newInstance(clinic);
        fragment.setPresenter(presenter);

        getFragmentManager().beginTransaction().add(R.id.fragment_clinic, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }

    @Override
    public void onSaveClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    @Override
    public void onViewReadingsClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_VIEW_READINGS);
    }

    @Override
    public void onAddReadingClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_ADD_READING);
    }
}
