package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.MainActivityState;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        machine.transition(new MainActivityState(machine, this));
    }

    public void onClick(View view) {
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();

        switch (view.getId()) {
            case R.id.button_clinics:
                machine.process(ClinicalTrialEvent.ON_CLINICS);
                break;
            case R.id.button_patients:
                break;
            case R.id.button_readings:
                break;
            case R.id.button_add_reading:
                break;
            case R.id.button_import:
                machine.process(ClinicalTrialEvent.ON_IMPORT);
                break;
            case R.id.button_export:
                break;
        }
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onPause() {
        super.onPause();
    }

}
