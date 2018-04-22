package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.AddClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.UpdateClinicState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicsActivity extends AppCompatActivity implements ClinicsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics);

        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        final ClinicalTrialModel model = machine.getApplication().getModel();
//        final ListView viewClinics = findViewById(R.id.clinics);
//        final Button add = findViewById(R.id.add);
//        try {
//            ArrayAdapter<Clinic> arrayAdapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_list_item_1, model.getClinics());
//            viewClinics.setAdapter(arrayAdapter);
//            viewClinics.setOnItemClickListener(this);
//        } catch (TrialCatalogException e) {
//            e.printStackTrace();
//        }
        ClinicsPresenter presenter = new ClinicsPresenter();

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
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == UpdateClinicState.UPDATE_CLINIC) {

            }

            if (requestCode == AddClinicState.ADD_CLINIC) {

            }
        }
    }
}
