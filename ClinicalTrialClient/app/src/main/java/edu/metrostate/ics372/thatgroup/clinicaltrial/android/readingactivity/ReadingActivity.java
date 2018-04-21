package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity.AddEditReadingActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.ReadingsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingActivity extends AppCompatActivity {
    Reading selectedReading;
    FloatingActionButton editReadingFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        editReadingFab = findViewById(R.id.edit_reading_fab);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            selectedReading = (Reading) getIntent().getSerializableExtra(ReadingFragment.READING_TAG);
            arguments.putSerializable(
                    ReadingFragment.READING_TAG, selectedReading);
            ReadingFragment fragment = new ReadingFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.reading_detail_container, fragment)
                    .commit();
        }
        setupListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ReadingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListeners() {
        editReadingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddEditReadingActivity.class);
                intent.putExtra(ReadingFragment.READING_TAG, selectedReading);
                System.out.println(selectedReading.toString());
                context.startActivity(intent);
            }
        });
    }
}
