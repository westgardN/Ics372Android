package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

public class AddEditReadingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_edit_reading_fragment, container, false);
    }
}
