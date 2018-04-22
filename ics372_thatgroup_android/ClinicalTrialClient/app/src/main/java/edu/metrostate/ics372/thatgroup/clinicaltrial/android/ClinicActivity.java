package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicActivity extends AppCompatActivity implements ClinicFragment.OnFragmentInteractionListener {

    private ClinicalTrialStateMachine machine;
    private ClinicalTrialModel model;
    private Clinic activeClinic;
//    private TextView textViewName;
//    private TextView textViewId;
//    private Button addReadingButton;
//    private Button viewReadingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();

        model = machine.getApplication().getModel();

        setContentView(R.layout.activity_clinic);
//        textViewName = findViewById (R.id.textViewName);
//        textViewId = findViewById (R.id.textViewId);
//        addReadingButton = findViewById (R.id.addReadingButton);
//        viewReadingsButton = findViewById (R.id.viewReadingsButton);

        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_clinic));

        if (obj instanceof Clinic) {
            ClinicPresenter presenter = new ClinicPresenter();
            Clinic clinic = (Clinic) obj;

            if (clinic != null) {
                presenter.setClinic(clinic);
                ClinicFragment fragment = ClinicFragment.newInstance(clinic);
                fragment.setPresenter(presenter);

                getFragmentManager().beginTransaction().add(R.id.fragment_clinic, fragment).commit();
            }
        }
//        try {
//            activeClinic = model.getClinic(intentID);
//        } catch (TrialCatalogException e) {
//            e.printStackTrace();
//        }
//        textViewName.setText(activeClinic.getName());
//        textViewId.setText(activeClinic.getId());

    }

    @Override
    public void onBackPressed() {
        super.onDestroy();
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
