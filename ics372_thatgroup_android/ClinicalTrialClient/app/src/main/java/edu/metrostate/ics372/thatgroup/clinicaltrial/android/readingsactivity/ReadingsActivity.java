package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ReadingsActivity extends AppCompatActivity implements ReadingsFragment.OnFragmentInteractionListener {
    private ReadingsPresenter presenter = null;
    private final String CLINIC_ARG = "clinic";
    private final String PATIENT_ARG = "patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        final ClinicalTrialModel model = machine.getApplication().getModel();
        final ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();
        state.setCurrentActivity(this);
        presenter = new ReadingsPresenter(machine);

        try {

            if (getIntent().hasExtra(getString(R.string.intent_view_readings_clinic))) {
                Clinic clinic = (Clinic) getIntent().getExtras().getSerializable(getString(R.string.intent_view_readings_clinic));
                presenter.setReadings(model.getJournal(clinic));
            } else if (getIntent().hasExtra(getString(R.string.intent_view_readings_patient))) {
                Patient patient = (Patient) getIntent().getExtras().getSerializable(getString(R.string.intent_view_readings_patient));
                presenter.setReadings(model.getJournal(patient));
            } else {
                presenter.setReadings(model.getReadings());
            }

            ReadingsFragment fragment = ReadingsFragment.newInstance();
            fragment.setPresenter(presenter);

            getFragmentManager().beginTransaction().add(R.id.fragment_readings, fragment).commit();
        } catch (TrialCatalogException e) {
        }
    }

    public void onClick(View view) {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();

        machine.process(ClinicalTrialEvent.ON_ADD);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                                    if (presenter != null) {
                                        presenter.addReading(reading);
                                    }
                                } else {
                                    msg = getString(R.string.reading_updated);
                                    if (presenter != null) {
                                        presenter.updateReading(reading);
                                    }
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
