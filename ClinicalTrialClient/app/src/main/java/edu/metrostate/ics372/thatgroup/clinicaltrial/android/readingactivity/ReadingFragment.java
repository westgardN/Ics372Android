package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;

public class ReadingFragment extends Fragment implements ReadingView,
        Button.OnClickListener, TextWatcher {

    private static final int MAX_READING_ID = 32;
    private static final String ARG_READING = "reading";

    private ReadingPresenter presenter;

    private Reading reading;

    private ReadingFragment.OnFragmentInteractionListener mListener;

    public static ReadingFragment newInstance(Reading reading) {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_READING, reading);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getSerializable(ARG_READING);
            if (obj instanceof Reading) {
                this.reading = (Reading) obj;
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);
        setDateAndTimePickerListeners(view);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.subscribe();
        }
        ((Button)getView().findViewById(R.id.reading_save_btn)).setOnClickListener(this);

        Spinner type = ((Spinner) getView().findViewById(R.id.reading_type_spinner));
        EditText date = ((EditText) getView().findViewById(R.id.reading_date_txt));
        EditText time = ((EditText) getView().findViewById(R.id.reading_time_txt));
        EditText value = ((EditText) getView().findViewById(R.id.reading_value_txt));
        date.setFocusable(false);
        time.setFocusable(false);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    value.setFocusable(false);
                    value.setOnClickListener(v -> {
                        DialogFragment bpDialog = new BloodPressureDialog();
                        bpDialog.show(getActivity().getFragmentManager(), "bpPicker");

                    });
                }
                if (position != 3) {
                    value.setOnClickListener(null);
                    value.setFocusable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //value.setFocusable(true);
            }
        });

        date.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePickDialog();

            // Show the time picker dialog fragment
            dialogFragment.show(getActivity().getFragmentManager(), "datePicker");
        });

        time.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePickerFragment();

            // Show the time picker dialog fragment
            dialogFragment.show(getActivity().getFragmentManager(), "timePicker");
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reading_save_btn:
                if (mListener != null) {
                    mListener.onSaveClicked();
                }
                break;
        }
    }

    @Override
    public void setPresenter(ReadingPresenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReadingFragment.OnFragmentInteractionListener) {
            mListener = (ReadingFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        presenter = null;
    }

    @Override
    public String getReadingId() {
        return ((EditText)getView().findViewById(R.id.reading_id_txt)).getText().toString();
    }

    @Override
    public String getClinicId() {
        return ((EditText)getView().findViewById(R.id.reading_clinic_id_txt)).getText().toString();
    }

    @Override
    public String getPatientId() {
        return ((EditText)getView().findViewById(R.id.reading_patient_id_txt)).getText().toString();
    }

    @Override
    public String getType() {
        return ((Spinner)getView().findViewById(R.id.reading_type_spinner)).getSelectedItem().toString();
    }

    @Override
    public String getValue() {
        return ((EditText)getView().findViewById(R.id.reading_value_txt)).getText().toString();
    }

    @Override
    public LocalDate getDate() {
        String date = ((TextView)getView().findViewById(R.id.reading_date_txt)).getText().toString();
        LocalDate answer = null;

        if (date != null && !date.isEmpty()) {
            try {
                answer = LocalDate.parse(date);
            } catch (DateTimeParseException ex) {

            }
        }

        return answer;
        //return LocalDate.parse(((EditText)getView().findViewById(R.id.reading_date_txt)).getText().toString());
    }

    @Override
    public LocalTime getTime() {
        return LocalTime.parse(((EditText)getView().findViewById(R.id.reading_time_txt)).getText().toString());
    }

    @Override
    public void setReadingId(String id) {
        ((EditText)getView().findViewById(R.id.reading_id_txt)).setText(id);
    }

    @Override
    public void setClinicId(String clinicId) {
        ((EditText)getView().findViewById(R.id.reading_clinic_id_txt)).setText(clinicId);
    }

    @Override
    public void setPatientId(String patientId) {
        ((EditText)getView().findViewById(R.id.reading_patient_id_txt)).setText(patientId);
    }

    @Override
    public void setType(String type) {
        int i = 0;
        if (type.equals(Reading.class.getName())) {
            ((Spinner)getView().findViewById(R.id.reading_type_spinner)).setSelection(0);
            //TODO
        } else {
            for (String s : getResources().getStringArray(R.array.reading_types)) {
                if (type.equals(s)) {
                    break;
                } else {
                    i++;
                }
            }
            ((Spinner)getView().findViewById(R.id.reading_type_spinner)).setSelection(i);
        }
    }

    @Override
    public void setValue(String value) {
        ((EditText)getView().findViewById(R.id.reading_value_txt)).setText(value);
    }

    @Override
    public void setDate(LocalDate date) {
        ((EditText)getView().findViewById(R.id.reading_date_txt)).setText(date.toString());
    }

    @Override
    public void setTime(LocalTime time) {
        ((EditText)getView().findViewById(R.id.reading_time_txt)).setText(time.toString());
    }

    @Override
    public void setDisabledSave(boolean b) {

    }

    @Override
    public void setDateAndTimePickerListeners(View view) {

    }

    public interface OnFragmentInteractionListener {
        //void onTimeClicked();
        void onSaveClicked();
        void onInputError();
        void onInputOk();
    }
}
