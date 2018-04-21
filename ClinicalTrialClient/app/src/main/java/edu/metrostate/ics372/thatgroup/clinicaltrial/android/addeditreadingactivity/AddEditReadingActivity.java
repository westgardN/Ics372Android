package edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingFragment;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.ReadingsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class AddEditReadingActivity extends AppCompatActivity {
    public static final String ADD = "add";
    public static final String EDIT = "edit";
    private Object bean;
    private View rootView;
    private ClinicalTrialModel model;
    private AddEditReadingPresenter addEditReadingPresenter;
    private ReadingPresenter readingPresenter;
    private boolean hasClinic, hasPatient, hasReading;

    private TextView id;
    private TextView clinicId;
    private TextView patientId;
    private Spinner type;
    private TextView value;
    private TextView date;
    private TextView time;

    public AddEditReadingActivity() {
        bean = null;
        hasClinic = false;
        hasPatient = false;
        hasReading = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_reading);
        Bundle arguments = getIntent().getExtras();
        model = ((ClinicalTrialClient) getApplication()).getModel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = findViewById(R.id.add_edit_reading_id_txt);
        clinicId = findViewById(R.id.add_edit_reading_clinic_id_txt);
        patientId = findViewById(R.id.add_edit_reading_patient_id_txt);
        type = findViewById(R.id.add_edit_reading_type_spinner);
        value = findViewById(R.id.add_edit_reading_value_txt);
        date = findViewById(R.id.add_edit_reading_date_txt);
        time = findViewById(R.id.add_edit_reading_time_txt);

        if (arguments != null) {
            if (getIntent().getExtras().containsKey(ADD)) {
                setTitle("Add New Reading");
            } else if (getIntent().getExtras().containsKey(EDIT)) {
                setTitle("Edit Reading");
            }

            //arguments.putSerializable("clinic", new Clinic("foo", "bar", "that clinic")); // TODO erase after done
            if (arguments.containsKey(ReadingsActivity.CLINIC)) {
                bean = arguments.getSerializable(ReadingsActivity.CLINIC);
                hasClinic = true;
            } else if (arguments.containsKey(ReadingsActivity.PATIENT)) {
                bean = arguments.getSerializable(ReadingsActivity.PATIENT);
                hasPatient = true;
            } else if (arguments.containsKey(ReadingFragment.READING_TAG)) {
                bean = arguments.getSerializable(ReadingFragment.READING_TAG);
                hasReading = true;
            }

            if (hasReading) {
                readingPresenter = new ReadingPresenter((Reading) Objects.requireNonNull(bean));
            } else {
                addEditReadingPresenter = new AddEditReadingPresenter(bean, model);
            }
        }
        setListeners();
        setFormTextFields();
    }


    private void setFormTextFields() {
        if (hasClinic) {
            TextView clinicId = findViewById(R.id.add_edit_reading_clinic_id_txt);
            clinicId.setText(addEditReadingPresenter.getClinicId());
            clinicId.setEnabled(false);
        } else if (hasPatient) {
            TextView patientId = findViewById(R.id.add_edit_reading_patient_id_txt);
            patientId.setText(addEditReadingPresenter.getPatientId());
            patientId.setEnabled(false);
        } else if (hasReading) {
            setTitle(getResources().getString(R.string.title_edit_reading, readingPresenter.getId()));
            fillFormForReadingEdit();
        }
    }

    private void fillFormForReadingEdit() {
        String[] types = getResources().getStringArray(R.array.reading_types);
        String type = readingPresenter.getType();
        int selectionIndex = 0;

        for (String t : types) {
            System.out.println(t);
            if(type.equals(t)) {
                break;
            } else {
                selectionIndex++;
            }
        }
        id.setText(readingPresenter.getId());
        id.setEnabled(false);
        clinicId.setText(readingPresenter.getClinicId());
        patientId.setText(readingPresenter.getPatientId());
        this.type.setSelection(selectionIndex);
        value.setText(readingPresenter.getValue());
        date.setText(readingPresenter.getDate());
        time.setText(readingPresenter.getTime());
    }

    public void setListeners() {
        findViewById(R.id.add_edit_reading_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditReadingPresenter.addNewReading(
                        new String[] {
                                id.getText().toString(),
                                clinicId.getText().toString(),
                                patientId.getText().toString(),
                                type.getSelectedItem().toString(),
                                value.getText().toString(),
                                date.getText().toString(),
                                time.getText().toString()
                        }
                );
            }
        });
    }
}
