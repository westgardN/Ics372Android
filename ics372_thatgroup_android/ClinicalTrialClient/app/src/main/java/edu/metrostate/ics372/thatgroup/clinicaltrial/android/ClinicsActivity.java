package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.MainActivityState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicsActivity extends AppCompatActivity {

    private ListView uiclinicList;
    private ArrayList<String> clinicStringList;
    private List<Clinic> clinicsProperty;
    private ClinicalTrialStateMachine machine;
    private ClinicalTrialModel model;
    private PropertyChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        model = machine.getApplication().getModel();
        setContentView(R.layout.activity_clinics);

        uiclinicList = findViewById(R.id.clinicListView);

    }

    @Override
    protected void onResume(){
        super.onResume();
        clinicStringList = new ArrayList<>();
        try {
            clinicsProperty = model.getClinics();
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        for(int x=0; x<= clinicsProperty.size()-1; x++){
            clinicStringList.add(clinicsProperty.get(x).getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, clinicStringList);
        uiclinicList.setAdapter(arrayAdapter);
        uiclinicList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                Intent intent =  new Intent(ClinicsActivity.this,ClinicActivity.class);
                intent.putExtra("ClinicID",clinicsProperty.get(position).getId());
                startActivity(intent);
            }
        });

    }
    protected void newClinic(View view) {
        machine.process(ClinicalTrialEvent.ON_CLINICS);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }

}
