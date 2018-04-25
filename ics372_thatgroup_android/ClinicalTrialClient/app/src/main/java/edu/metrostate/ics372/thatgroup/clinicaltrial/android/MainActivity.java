package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.MainActivityState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState state = new MainActivityState(machine, this);
        state.setCurrentActivity(this);
        machine.transition(state);

    }

    public void onClick(View view) {
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();

        switch (view.getId()) {
            case R.id.button_clinics:
                machine.process(ClinicalTrialEvent.ON_CLINICS);
                break;
            case R.id.button_patients:
                machine.process(ClinicalTrialEvent.ON_PATIENTS);
                break;
            case R.id.button_readings:
                machine.process(ClinicalTrialEvent.ON_READINGS);
                break;
            case R.id.button_add_reading:
                machine.process(ClinicalTrialEvent.ON_ADD_READING);
                break;
            case R.id.button_import:
                machine.process(ClinicalTrialEvent.ON_IMPORT);
                break;
            case R.id.button_export:
                machine.process(ClinicalTrialEvent.ON_EXPORT);
                break;
        }
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onPause() {
        super.onPause();
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
