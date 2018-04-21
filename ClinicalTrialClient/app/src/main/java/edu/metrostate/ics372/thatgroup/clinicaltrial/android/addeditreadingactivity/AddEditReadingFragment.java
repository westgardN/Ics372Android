package edu.metrostate.ics372.thatgroup.clinicaltrial.android.addeditreadingactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Objects;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingFragment;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity.ReadingPresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.ReadingsActivity;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;

public class AddEditReadingFragment extends Fragment {
    private Object bean;
    private View rootView;
    private ClinicalTrialModel model;
    private AddEditReadingPresenter addEditReadingPresenter;
    private ReadingPresenter readingPresenter;
    boolean hasClinic, hasPatient, hasReading;
    public AddEditReadingFragment() {
        bean = null;
        //model = ((ClinicalTrialClient) ctx.getApplicationContext()).getModel();
        hasClinic = false;
        hasPatient = false;
        hasReading = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ReadingsActivity.CLINIC)) {
                bean = getArguments().getSerializable(ReadingsActivity.CLINIC);
                hasClinic = true;
            } else if (arguments.containsKey(ReadingsActivity.PATIENT)) {
                bean = getArguments().getSerializable(ReadingsActivity.PATIENT);
                hasPatient = true;
            } else if (arguments.containsKey(ReadingFragment.READING_TAG)) {
                bean = getArguments().getSerializable(ReadingFragment.READING_TAG);
                hasReading = true;
            }
            if (hasReading) {
                readingPresenter = new ReadingPresenter((Reading) Objects.requireNonNull(bean));
            } else {
                addEditReadingPresenter = new AddEditReadingPresenter(bean, model);
            }
        }
        registerListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_edit_reading_fragment, container, false);
        setFormTextFields();
        return rootView;
    }

    private void setFormTextFields() {
        if (hasClinic) {
            TextView clinicId = rootView.findViewById(R.id.add_edit_reading_clinic_id_txt);
            clinicId.setText(addEditReadingPresenter.getClinicId());
            clinicId.setEnabled(false);
        } else if (hasPatient) {
            TextView patientId = rootView.findViewById(R.id.add_edit_reading_patient_id_txt);
            patientId.setText(addEditReadingPresenter.getPatientId());
            patientId.setEnabled(false);
        } else if (hasReading) {

        }
    }

    private void registerListeners() {

    }
}
