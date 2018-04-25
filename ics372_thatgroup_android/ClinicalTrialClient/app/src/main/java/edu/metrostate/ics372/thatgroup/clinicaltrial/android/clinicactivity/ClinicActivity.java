package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicErrorState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

/**
 * The Clinic Activity allows the user to update the selected clinic or add a reading or view
 * readings for the clinc.
 *
 * @author That Group
 */
public class ClinicActivity extends AppCompatActivity implements ClinicFragment.OnFragmentInteractionListener {
    private ClinicalTrialStateMachine machine;
    private ClinicPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);

        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState current = (ClinicalTrialState) machine.getCurrentState();
        current.setCurrentActivity(this);
        presenter = new ClinicPresenter(machine);

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

    /**
     *
     */
    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }

    /**
     *
     */
    @Override
    public void onSaveClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    /**
     *
     */
    @Override
    public void onViewReadingsClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_VIEW_READINGS);
    }

    /**
     *
     */
    @Override
    public void onAddReadingClicked() {
        ClinicState state = (ClinicState)machine.getCurrentState();

        state.setClinic(presenter.getClinic());
        machine.process(ClinicalTrialEvent.ON_ADD_READING);
    }

    /**
     *
     */
    @Override
    public void onInputError() {
        machine.process(ClinicalTrialEvent.ON_ERROR);
        presenter.updateView(false);
    }

    /**
     *
     */
    @Override
    public void onInputOk() {
        if (machine.getCurrentState() instanceof ClinicErrorState) {
            machine.process(ClinicalTrialEvent.ON_OK);
        }
        presenter.updateView(false);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        final ClinicalTrialModel model = machine.getApplication().getModel();

        if (resultCode == RESULT_OK) {
            if (requestCode == ReadingState.UPDATE_READING || requestCode == ReadingState.ADD_READING) {
                if (data != null) {
                    Object obj = data.getSerializableExtra(getResources().getString(R.string.intent_updated_or_added));
                    if (obj instanceof Reading) {
                        Reading reading = (Reading) obj;

                        try {
                            String msg = "";
                            if (model.updateOrAdd(reading)) {
                                if (requestCode == ReadingState.ADD_READING) {
                                    msg = getString(R.string.reading_added);
                                } else {
                                    msg = getString(R.string.reading_updated);
                                }

                                msg += " " + reading.getId();
                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        } catch (TrialCatalogException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
