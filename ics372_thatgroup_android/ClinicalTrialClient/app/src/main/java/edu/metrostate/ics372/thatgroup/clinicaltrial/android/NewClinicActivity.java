package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicErrorState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class NewClinicActivity extends AppCompatActivity {

    private ClinicalTrialModel model;
    private ClinicalTrialStateMachine machine;
    private TextView editTextName;
    private TextView editTextID;
    private TextView errorTextView;
    public Button addClinicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        model = machine.getApplication().getModel();
        setContentView(R.layout.activity_new_clinic);
        editTextName = findViewById(R.id.editTextName);
        editTextID = findViewById(R.id.editTextID);
        addClinicButton = findViewById(R.id.addClinicButton);
        errorTextView = findViewById(R.id.errorTextView);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(validate(editTextName.getText().toString(),200,true)
                        && (validate(editTextID.getText().toString(),200,false)))) {
                    machine.process(ClinicalTrialEvent.ON_ERROR);
                } else {
                    if (machine.getCurrentState() instanceof ClinicErrorState) {
                        machine.process(ClinicalTrialEvent.ON_OK);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(validate(editTextName.getText().toString(),200,true)
                        && (validate(editTextID.getText().toString(),200,false)))) {
                    machine.process(ClinicalTrialEvent.ON_ERROR);
                } else {
                    machine.process(ClinicalTrialEvent.ON_OK);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }

    private boolean validate(String text, int maxLength, boolean allowSpace) {
        boolean answer = false;
        String matchString = allowSpace ? getString(R.string.regex_no_special_chars_allow_spaces) : getString(R.string.regex_no_special_chars);

        if (text != null && !text.trim().isEmpty() && text.trim().length() <= maxLength) {
            if (text.matches(matchString)) {
                answer = true;
            }
        }

        return answer;
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
