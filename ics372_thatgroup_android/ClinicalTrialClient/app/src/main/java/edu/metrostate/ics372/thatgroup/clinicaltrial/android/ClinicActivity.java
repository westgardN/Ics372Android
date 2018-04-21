package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicActivity extends AppCompatActivity {

    private ClinicalTrialStateMachine machine;
    private ClinicalTrialModel model;
    private Clinic activeClinic;
    private List<Reading> allReadings;
    private List<Reading> activeClinicReadings;
    private ArrayList<String> activeClinicReadingsString;
    private TextView textViewName;
    private TextView textViewId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        model = machine.getApplication().getModel();

        setContentView(R.layout.activity_clinic);
        textViewName = findViewById (R.id.textViewName);
        textViewId = findViewById (R.id.textViewId);


        Intent intent = getIntent();
        String intentID = intent.getStringExtra("ClinicID");
        try {
            activeClinic = model.getClinic(intentID);
            allReadings = model.getReadings();
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        textViewName.setText(activeClinic.getName());
        textViewId.setText(activeClinic.getId());
        for(int x=1; x<=allReadings.size(); x++){
            if(allReadings.get(x).getClinicId().equals( activeClinic.getId())){
                activeClinicReadings.add(allReadings.get(x));
                activeClinicReadingsString.add(allReadings.get(x).getId());
            }
        }

    }

}
