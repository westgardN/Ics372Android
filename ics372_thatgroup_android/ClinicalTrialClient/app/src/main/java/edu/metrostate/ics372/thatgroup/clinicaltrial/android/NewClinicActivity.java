package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class NewClinicActivity extends AppCompatActivity {

    private ClinicalTrialModel model;
    private ClinicalTrialStateMachine machine;
    private TextView editTextName;
    private TextView editTextID;
    private Button addClinicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        model = machine.getApplication().getModel();
        setContentView(R.layout.activity_new_clinic);
        editTextName = findViewById(R.id.editTextName);
        editTextID = findViewById(R.id.editTextID);
        addClinicButton = findViewById(R.id.addClinicButton);

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


    protected void addClinic(View view){
        try {
            Clinic clinic = new Clinic(editTextID.getText().toString(), null, editTextName.getText().toString());
            if (model.updateOrAdd(clinic)) {
                Toast.makeText(getApplicationContext(),getString(R.string.clinic_added) +" "+ editTextName.getText().toString(), Toast.LENGTH_SHORT).show();
                editTextID.setText("");
                editTextName.setText("");
            } else {

                Toast.makeText(getApplicationContext(), getString(R.string.err_clinic_not_added), Toast.LENGTH_SHORT).show();
            }
        } catch (TrialCatalogException e) {

                Toast.makeText(getApplicationContext(), getString(R.string.err_clinic_not_added), Toast.LENGTH_SHORT).show();
        }
    }



}
