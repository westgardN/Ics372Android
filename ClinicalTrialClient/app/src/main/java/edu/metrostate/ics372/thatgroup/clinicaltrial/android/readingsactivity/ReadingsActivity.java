package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.dummy.DummyContent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter.ReadingsPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * An activity representing a list of Readings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ReadingActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ReadingsActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    ClinicalTrialModel model;
    ReadingsPresenter readingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
        List<Reading> readings = null;
        ClinicalTrialModel model;
        ReadingsPresenter readingsPresenter;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.reading_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

//        try {
//            model = ((ClinicalTrialClient)getApplication()).getModel();
//            if (savedInstanceState != null) {
//                if (checkBundleForBean(savedInstanceState)) {
//                    Object bean = getBeanFromBundle(savedInstanceState);
//                    if (bean instanceof Clinic) {
//                        readingsPresenter = new ReadingsPresenter(model, ((Clinic) bean));
//                        readings = readingsPresenter.getReadings();
//                    } else if (bean instanceof Patient) {
//                        readingsPresenter = new ReadingsPresenter(model, ((Patient) bean));
//                        readings = readingsPresenter.getReadings();
//                    }
//                }
//            } else {
//                readingsPresenter = new ReadingsPresenter(model);
//                readings = readingsPresenter.getReadings();
//            }
//        } catch (TrialCatalogException e) {
//            e.printStackTrace();
//        }

        View recyclerView = findViewById(R.id.reading_list);
        assert recyclerView != null;
        List<Reading> foo = new LinkedList<>();
        Reading reading = ReadingFactory.getReading("weight");
        reading.setId("fhfy43");
        reading.setClinicId("foo");
        reading.setValue("195");
        reading.setPatientId("bar");
        reading.setDate(LocalDateTime.now());
        foo.add(reading);
        setupRecyclerView((RecyclerView) recyclerView, foo, savedInstanceState);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Reading> readings, Bundle bundle) {
        try {
            model = ((ClinicalTrialClient)getApplication()).getModel();
            if (bundle != null) {
                if (checkBundleForBean(bundle)) {
                    Object bean = getBeanFromBundle(bundle);
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
        recyclerView.setAdapter(new ReadingListAdapter(this, readings, mTwoPane));
    }

    private Object getBeanFromBundle(Bundle bundle) {
        return bundle.containsKey("clinic") ? bundle.getSerializable("clinic") : bundle.getSerializable("patient");
    }

    private boolean checkBundleForBean(Bundle bundle) {
        return bundle.containsKey("clinic") || bundle.containsKey("patient");
    }

//    public static class SimpleItemRecyclerViewAdapter
//            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private final ReadingsActivity mParentActivity;
//        private final List<Reading> mValues;
//        private final boolean mTwoPane;
//        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Reading reading = (Reading) view.getTag();
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ReadingDetailFragment.ARG_ITEM_ID, reading.getId());
//                    ReadingDetailFragment fragment = new ReadingDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.reading_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, ReadingActivity.class);
//                    intent.putExtra(ReadingDetailFragment.ARG_ITEM_ID, reading.getId());
//                    context.startActivity(intent);
//                }
//            }
//        };
//
//        SimpleItemRecyclerViewAdapter(ReadingsActivity parent,
//                                      List<Reading> readings,
//                                      boolean twoPane) {
//            mValues = readings;
//            mParentActivity = parent;
//            mTwoPane = twoPane;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.reading_list_content, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).getId());
//           // holder.mContentView.setText(mValues.get(position));
//
//            holder.itemView.setTag(mValues.get(position));
//            holder.itemView.setOnClickListener(mOnClickListener);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mValues.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder {
//            final TextView mIdView;
//            final TextView mContentView;
//
//            ViewHolder(View view) {
//                super(view);
//                mIdView = (TextView) view.findViewById(R.id.id_text);
//                mContentView = (TextView) view.findViewById(R.id.content);
//            }
//        }
//    }
}
