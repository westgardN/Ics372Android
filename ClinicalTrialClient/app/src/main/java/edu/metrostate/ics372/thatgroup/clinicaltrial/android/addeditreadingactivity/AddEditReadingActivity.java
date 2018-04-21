package edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class AddEditReadingActivity extends AppCompatActivity {
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    private Object bean;
    private View rootView;
    private ClinicalTrialModel model;
    private AddEditReadingPresenter addEditReadingPresenter;
    private ReadingPresenter readingPresenter;
    boolean hasClinic, hasPatient, hasReading;

    public AddEditReadingActivity() {
        bean = null;
        //model = ((ClinicalTrialClient) getApplication()).getModel();
        hasClinic = false;
        hasPatient = false;
        hasReading = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_reading);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras().containsKey(ADD)) {
            setTitle("Add New Reading");
        } else if (getIntent().getExtras().containsKey(EDIT)) {
            setTitle("Edit Reading");
        }
    }
}
