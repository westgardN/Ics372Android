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
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.AddClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.UpdateClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicsActivity extends AppCompatActivity implements ClinicsFragment.OnFragmentInteractionListener {
    ClinicsPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics);

        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        final ClinicalTrialModel model = machine.getApplication().getModel();
        final ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();
        state.setCurrentActivity(this);
        presenter = new ClinicsPresenter();

        try {

            presenter.setClinics(model.getClinics());

            ClinicsFragment fragment = ClinicsFragment.newInstance();
            fragment.setPresenter(presenter);

            getFragmentManager().beginTransaction().add(R.id.fragment_clinics, fragment).commit();
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
            if (requestCode == ClinicState.UPDATE_CLINIC || requestCode == ClinicState.ADD_CLINIC) {
                if (data != null) {
                    Object obj = data.getSerializableExtra(getResources().getString(R.string.intent_updated_or_added));
                    if (obj instanceof Clinic) {
                        Clinic clinic = (Clinic) obj;

                        try {
                            String msg = "";
                            if (model.updateOrAdd(clinic)) {
                                if (requestCode == ClinicState.ADD_CLINIC) {
                                    msg = getString(R.string.clinic_added);
                                    if (presenter != null) {
                                        presenter.addClinic(clinic);
                                    }
                                } else {
                                    msg = getString(R.string.clinic_updated);
                                    if (presenter != null) {
                                        presenter.updateClinic(clinic);
                                    }
                                }

                                msg += " " + clinic.getName();
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
