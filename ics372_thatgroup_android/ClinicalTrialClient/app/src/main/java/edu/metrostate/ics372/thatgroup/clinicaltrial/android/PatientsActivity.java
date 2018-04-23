package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class PatientsActivity extends AppCompatActivity implements PatientsFragment.OnFragmentInteractionListener{
    PatientsPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        final ClinicalTrialModel model = machine.getApplication().getModel();
        final ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();
        state.setCurrentActivity(this);
        presenter = new PatientsPresenter();

        try {

            presenter.setPatients(model.getPatients());

            PatientsFragment fragment = PatientsFragment.newInstance();
            fragment.setPresenter(presenter);

            getFragmentManager().beginTransaction().add(R.id.fragment_patients, fragment).commit();
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
            if (requestCode == PatientState.UPDATE_PATIENT || requestCode == PatientState.ADD_PATIENT) {
                if (data != null) {
                    Object obj = data.getSerializableExtra(getResources().getString(R.string.intent_updated_or_added));
                    if (obj instanceof Patient) {
                        Patient patient = (Patient) obj;

                        try {
                            String msg = "";
                            if (model.updateOrAdd(patient)) {
                                if (requestCode == PatientState.ADD_PATIENT) {
                                    msg = getString(R.string.patient_added);
                                    if (presenter != null) {
                                        presenter.addPatient(patient);
                                    }
                                } else {
                                    msg = getString(R.string.patient_updated);
                                    if (presenter != null) {
                                        presenter.updatePatient(patient);
                                    }
                                }

                                msg += " " + patient.getId();
                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        } catch (TrialCatalogException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }
}
