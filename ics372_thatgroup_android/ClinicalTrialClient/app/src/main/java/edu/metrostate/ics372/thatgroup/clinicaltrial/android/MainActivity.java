package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClinicalTrialModel model = ((ClinicalTrialClient)getApplication()).getModel();
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onPause() {
        super.onPause();
    }




}
