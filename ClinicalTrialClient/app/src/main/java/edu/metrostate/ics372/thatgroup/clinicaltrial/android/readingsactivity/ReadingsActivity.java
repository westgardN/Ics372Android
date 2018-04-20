package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter.ReadingsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity {

    private static final String CLINIC = "clinic";
    private static final String PATIENT = "patient";
    private boolean twoPane;
    private ReadingsPresenter readingsPresenter;
    private List<Reading> readings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        ClinicalTrialModel model = ((ClinicalTrialClient) getApplication()).getModel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.reading_detail_container) != null) {
            twoPane = true;
        }

        View recyclerView = findViewById(R.id.reading_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) findViewById(R.id.reading_list), model);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, ClinicalTrialModel model) {
        Bundle intentBundle = getIntent().getExtras();
        try {
            if (intentBundle != null) {
                if (checkBundleForBean(intentBundle)) {
                    Object bean = getBeanFromBundle(intentBundle);
                    if (bean instanceof Clinic) {
                        readingsPresenter = new ReadingsPresenter(model, ((Clinic) bean));
                        readings = readingsPresenter.getReadings();
                    } else if (bean instanceof Patient) {
                        readingsPresenter = new ReadingsPresenter(model, ((Patient) bean));
                        readings = readingsPresenter.getReadings();
                    }
                }
            } else {
                readingsPresenter = new ReadingsPresenter(model);
                readings = readingsPresenter.getReadings();
            }
        } catch (TrialCatalogException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(new ReadingListAdapter(this, readings, twoPane, readingsPresenter));
    }

    private Object getBeanFromBundle(Bundle bundle) {
        return bundle.containsKey(CLINIC) ? bundle.getSerializable(CLINIC) : bundle.getSerializable(PATIENT);
    }

    private boolean checkBundleForBean(Bundle bundle) {
        return bundle.containsKey(CLINIC) || bundle.containsKey(PATIENT);
    }
}
