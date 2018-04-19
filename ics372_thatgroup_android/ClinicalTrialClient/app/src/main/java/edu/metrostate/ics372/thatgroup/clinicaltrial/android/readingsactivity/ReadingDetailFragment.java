package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.dummy.DummyContent;

/**
 * A fragment representing a single Reading detail screen.
 * This fragment is either contained in a {@link ReadingsActivity}
 * in two-pane mode (on tablets) or a {@link ReadingActivity}
 * on handsets.
 */
public class ReadingDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReadingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reading_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.reading_datail_clinic_id)).setText(getString(R.string.reading_detail_clinic_id_lbl, "ghdty4"));
            ((TextView) rootView.findViewById(R.id.reading_detail_patient_id)).setText(getString(R.string.reading_detail_patient_id_lbl, "foh574"));
            ((TextView) rootView.findViewById(R.id.reading_detail_type)).setText(getString(R.string.reading_detail_type_lbl, "Weight"));
            ((TextView) rootView.findViewById(R.id.reading_detail_value)).setText(getString(R.string.reading_detail_value_lbl, "195"));
            ((TextView) rootView.findViewById(R.id.reading_detail_date)).setText(getString(R.string.reading_detail_date_lbl, "12/25/2018"));
            ((TextView) rootView.findViewById(R.id.reading_detail_time)).setText(getString(R.string.reading_detail_time_lbl, 12, 25, 26, "P.M."));
        }

        return rootView;
    }
}
