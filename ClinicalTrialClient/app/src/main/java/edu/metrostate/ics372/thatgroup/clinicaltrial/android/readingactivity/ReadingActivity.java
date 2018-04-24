package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.time.LocalDateTime;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
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

        if (intent.hasExtra(getResources().getString(R.string.intent_update_reading))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_reading));

            if (obj instanceof Reading) {
                reading = (Reading) obj;
            }
        }

        if (reading == null) {
            reading = ReadingFactory.getReading(ReadingFactory.WEIGHT);
            reading.setValue(0);
            reading.setDate(LocalDateTime.now());
        }

        presenter.setReading(reading);
        ReadingFragment fragment = ReadingFragment.newInstance(reading);
        fragment.setPresenter(presenter);

        getFragmentManager().beginTransaction().add(R.id.fragment_reading_holder, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }



    public void setDateAndTimePickerListeners() {
//        EditText time = ((EditText)findViewById(R.id.reading_time_txt));
//        time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment dFragment = new TimePickerFragment();
//
//                // Show the time picker dialog fragment
//                dFragment.show(getSupportFragmentManager(), "timePicker");
//            }
//        });
    }

    @Override
    public void onSaveClicked() {
        ReadingState state = (ReadingState)machine.getCurrentState();

        state.setReading(presenter.getReading());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    @Override
    public void onInputError() {

    }

    @Override
    public void onInputOk() {

    }
}
