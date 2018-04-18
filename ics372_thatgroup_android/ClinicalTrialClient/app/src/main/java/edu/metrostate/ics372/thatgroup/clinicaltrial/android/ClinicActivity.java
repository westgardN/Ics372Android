package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class ClinicActivity extends AppCompatActivity {

    private ListView uiclinicList;
    private ArrayList<String> clinicStringList;
    private List<Clinic> clinicsProperty;
    private ClinicalTrialModel model;
    private PropertyChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic);
        uiclinicList = findViewById(R.id.clinicListView);
        clinicStringList = new ArrayList<>();

        try {
            model = new ClinicalTrialModel("android", getApplicationContext().getFilesDir().toString());
        } catch (TrialCatalogException e) {
            //catch statement
        }


        //array adapter & click listener test
        for(int x=1; x<20; x++){
            clinicStringList.add("test "+x);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, clinicStringList);
        uiclinicList.setAdapter(arrayAdapter);
        uiclinicList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                String selectedIndex = clinicStringList.get(position);
                Toast.makeText(getApplicationContext(), "Index Selected : "+selectedIndex,   Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void newClinic(View view) {
        String newClinicClicked = "new Clinic Clicked";
        Toast.makeText(getApplicationContext(), newClinicClicked,   Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
