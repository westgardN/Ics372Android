package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicActivity extends AppCompatActivity {

    private ClinicalTrialStateMachine machine;
    private ClinicalTrialModel model;
    private Clinic activeClinic;
    private TextView textViewName;
    private TextView textViewId;
    private Button addReadingButton;
    private Button viewReadingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        model = machine.getApplication().getModel();

        setContentView(R.layout.activity_clinic);
        textViewName = findViewById (R.id.textViewName);
        textViewId = findViewById (R.id.textViewId);
        addReadingButton = findViewById (R.id.addReadingButton);
        viewReadingsButton = findViewById (R.id.viewReadingsButton);

        Intent intent = getIntent();
        String intentID = intent.getStringExtra("ClinicID");
        try {
            activeClinic = model.getClinic(intentID);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        textViewName.setText(activeClinic.getName());
        textViewId.setText(activeClinic.getId());

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }
}
