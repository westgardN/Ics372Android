package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity.AddEditReadingActivity;
//import edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity.AddEditReadingFragment;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingFragment;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    public static final String CLINIC = "clinic";
    public static final String PATIENT = "patient";
    public static final String ADD_EDIT_READING = "addEditReadingFrag";
    private boolean twoPane;
    private ReadingsPresenter readingsPresenter;
    private List<Reading> readings;
    private View.OnClickListener readingOnClickListener;
    FloatingActionButton addNewReadingFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);
        ClinicalTrialModel model = ((ClinicalTrialClient) getApplication()).getModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        addNewReadingFab = findViewById(R.id.add_new_reading_fab);

        if (findViewById(R.id.reading_detail_container) != null) {
            twoPane = true;
        }

        View recyclerView = findViewById(R.id.reading_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) findViewById(R.id.reading_list), model);
        setupListeners();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, ClinicalTrialModel model) {
        Bundle intentBundle = getIntent().getExtras();
        try {
            if (intentBundle != null) {
                Object bean = getBeanFromBundle(intentBundle);
                if (bean instanceof Clinic) {
                    readingsPresenter = new ReadingsPresenter(model, ((Clinic) bean));
                    readings = readingsPresenter.getReadings();
                } else if (bean instanceof Patient) {
                    readingsPresenter = new ReadingsPresenter(model, ((Patient) bean));
                    readings = readingsPresenter.getReadings();
                }
            } else {
                readingsPresenter = new ReadingsPresenter(model);
                readings = readingsPresenter.getReadings();
            }
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        List<Reading> readingsTestList = new LinkedList<>();
        Reading readingTest = ReadingFactory.getReading("weight");
        readingTest.setId("TEST");
        readingTest.setClinicId("FOO");
        readingTest.setPatientId("BAR");
        readingTest.setValue("125");
        readingTest.setDate(LocalDateTime.now());
        readingsTestList.add(readingTest);
        recyclerView.setAdapter(new ReadingListAdapter(this, readingsTestList, readingsPresenter));
    }

    private Object getBeanFromBundle(Bundle bundle) {
        return bundle.containsKey(CLINIC) ? bundle.getSerializable(CLINIC) : bundle.getSerializable(PATIENT);
    }

    public View.OnClickListener getReadingOnClickListener() {
        return readingOnClickListener;
    }

    public void setupListeners() {

        readingOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reading reading = (Reading) view.getTag();
                readingsPresenter.setActiveReading(reading);
//                if (twoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putSerializable(ReadingFragment.READING_TAG, reading);
//                    ReadingFragment fragment = new ReadingFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.reading_detail_container, fragment)
//                            .commit();
//                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ReadingActivity.class);
                    intent.putExtra(ReadingFragment.READING_TAG, reading);
                    context.startActivity(intent);
                //}
            }
        };

        addNewReadingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddEditReadingActivity.class);
                intent.putExtra(AddEditReadingActivity.ADD, AddEditReadingActivity.ADD);
                context.startActivity(intent);
            }
        });
    }
}
