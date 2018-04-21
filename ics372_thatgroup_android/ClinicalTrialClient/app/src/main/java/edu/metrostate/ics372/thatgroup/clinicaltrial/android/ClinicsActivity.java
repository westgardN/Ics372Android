package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicsActivityState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.MainActivityState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicsActivity extends AppCompatActivity {

    private ListView uiclinicList;
    private ArrayList<String> clinicStringList;
    private List<Clinic> clinicsProperty;
    private ClinicalTrialModel model;
    private PropertyChangeListener listener;
    private ClinicalTrialStateMachine machine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        setContentView(R.layout.activity_clinics);

        uiclinicList = findViewById(R.id.clinicListView);
        clinicStringList = new ArrayList<>();
        model = ((ClinicalTrialClient)getApplication()).getModel();

        try {
            model.addClinic("ID1","Name1");
            model.addClinic("ID2","Name2");
            model.addClinic("ID3","Name3");
            model.addClinic("ID4","Name4");
            model.addClinic("ID5","Name5");
            model.addClinic("ID6","Name6");
            model.addPatient("patient1",LocalDate.now());
            clinicsProperty = model.getClinics();
            for(int x=0; x<= clinicsProperty.size()-1; x++){
                clinicStringList.add(clinicsProperty.get(x).getName());
            }
        } catch (TrialCatalogException e) {
            e.printStackTrace();
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
        Intent intent = new Intent(ClinicsActivity.this,NewClinicActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        //machine.transition(new MainActivityState(machine,this));
    }

    @Override
    public void onDestroy() {
        model.removePropertyChangeListener(listener);
        super.onDestroy();
    }

}
