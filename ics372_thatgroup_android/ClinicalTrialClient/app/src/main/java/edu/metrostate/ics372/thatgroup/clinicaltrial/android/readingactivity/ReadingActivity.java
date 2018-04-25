package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingErrorState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

public class ReadingActivity extends AppCompatActivity implements ReadingFragment.OnFragmentInteractionListener {
    private ClinicalTrialStateMachine machine;
    private ReadingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState current = (ClinicalTrialState) machine.getCurrentState();
        current.setCurrentActivity(this);
        presenter = new ReadingPresenter(machine);

        Intent intent = getIntent();
        Reading reading = null;
        boolean updating = false;
        boolean addPatient = false;
        boolean addClinic = false;

        if (intent.hasExtra(getResources().getString(R.string.intent_update_reading))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_reading));

            if (obj instanceof Reading) {
                reading = (Reading) obj;
                updating = true;
            }
        } else if (intent.hasExtra(getResources().getString(R.string.intent_add_reading_clinic))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_add_reading_clinic));

            if (obj instanceof Clinic) {
                reading = ReadingFactory.getReading(ReadingFactory.WEIGHT);
                reading.setClinicId(((Clinic) obj).getId());
                addClinic = true;
            }
        } else if (intent.hasExtra(getResources().getString(R.string.intent_add_reading_patient))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_add_reading_patient));

            if (obj instanceof Patient) {
                reading = ReadingFactory.getReading(ReadingFactory.WEIGHT);
                reading.setPatientId(((Patient) obj).getId());
                addPatient = true;
            }
        }

        String param = Strings.EMPTY;

        if (updating) {
            param = getString(R.string.update_reading);
        } else if (addPatient) {
            param = getString(R.string.add_reading_patient);
        } else if (addClinic) {
            param = getString(R.string.add_reading_clinic);
        } else {
            param = getString(R.string.add_reading);
        }

        if (reading == null) {
            reading = ReadingFactory.getReading(ReadingFactory.WEIGHT);
        }

        presenter.setReading(reading);
        ReadingFragment fragment = ReadingFragment.newInstance(reading, param);
        fragment.setPresenter(presenter);

        getFragmentManager().beginTransaction().add(R.id.fragment_reading, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }

    @Override
    public void onSaveClicked() {
        ReadingState state = (ReadingState)machine.getCurrentState();

        state.setReading(presenter.getReading());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    @Override
    public void onInputError() {
        machine.process(ClinicalTrialEvent.ON_ERROR);
        presenter.updateView(false);
    }

    @Override
    public void onInputOk() {
        if (machine.getCurrentState() instanceof ReadingErrorState) {
            machine.process(ClinicalTrialEvent.ON_OK);
        }
        presenter.updateView(false);
    }
}
