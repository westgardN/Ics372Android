package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.Serializable;
import java.util.Objects;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingFragment extends Fragment {

    public static final String READING_TAG = "reading";
    private Serializable reading;
    private ReadingPresenter readingPresenter;

    public ReadingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Objects.requireNonNull(getArguments()).containsKey(READING_TAG)) {
            readingPresenter = new ReadingPresenter(((Reading) Objects.requireNonNull(getArguments()
                    .getSerializable(READING_TAG))));
            reading = getArguments().getSerializable(READING_TAG);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = Objects.requireNonNull(activity).findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(readingPresenter.getId());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reading_detail_fragment, container, false);

        if (reading != null) {
            ((TextView) rootView.findViewById(R.id.reading_detail_clinic_id)).setText(
                    getString(R.string.reading_detail_clinic_id_lbl, readingPresenter.getClinicId()));
            ((TextView) rootView.findViewById(R.id.reading_detail_patient_id)).setText(
                    getString(R.string.reading_detail_patient_id_lbl, readingPresenter.getPatientId()));
            ((TextView) rootView.findViewById(R.id.reading_detail_type)).setText(
                    getString(R.string.reading_detail_type_lbl, readingPresenter.getType()));
            ((TextView) rootView.findViewById(R.id.reading_detail_value)).setText(
                    getString(R.string.reading_detail_value_lbl, readingPresenter.getValue()));
            ((TextView) rootView.findViewById(R.id.reading_detail_date)).setText(
                    getString(R.string.reading_detail_date_lbl, readingPresenter.getDate()));
            ((TextView) rootView.findViewById(R.id.reading_detail_time)).setText(
                    getString(R.string.reading_detail_time_lbl, readingPresenter.getTime()));
        }

        return rootView;
    }
}
