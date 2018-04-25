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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

public class ReadingFragment extends Fragment implements ReadingView,
        Button.OnClickListener, TextWatcher {

    private static final int MAX_READING_ID = 32;
    private static final String ARG_READING = "reading";
    private static final String ARG_ACTION = "action";

    private ReadingPresenter presenter;

    private Reading reading;
    private String action;

    private ReadingFragment.OnFragmentInteractionListener mListener;

    public static ReadingFragment newInstance(Reading reading, String action) {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_READING, reading);
        args.putString(ARG_ACTION, action);
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

            action = getArguments().getString(ARG_ACTION);
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
        ((Button)getView().findViewById(R.id.save_reading)).setOnClickListener(this);

        try {
            loadClinicsSpinner();
            loadPatientsSpinner();
        } catch (TrialCatalogException ex) {
            ex.printStackTrace();
        }

        loadTypeSpinner();
        EditText date = ((EditText) getView().findViewById(R.id.reading_date));
        EditText time = ((EditText) getView().findViewById(R.id.reading_time));
        EditText value = ((EditText) getView().findViewById(R.id.reading_value));
        ((TextView)getView().findViewById(R.id.reading_id)).addTextChangedListener(this);
        value.setOnClickListener(this);

        date.setFocusable(false);
        time.setFocusable(false);

        date.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePickDialog();
            dialogFragment.show(getActivity().getFragmentManager(), getString(R.string.tag_date));
        });

        time.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePickerFragment();
            dialogFragment.show(getActivity().getFragmentManager(), getString(R.string.tag_time));
        });
    }

    private void loadClinicsSpinner() throws TrialCatalogException {
        Spinner spinnerClinic = ((Spinner) getView().findViewById(R.id.reading_clinic));
        Clinic[] clinics = null;
        if (action != null && action.equals(getString(R.string.add_reading_clinic))) {
            Clinic clinic;
            clinic = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getClinic(reading.getClinicId());
            clinics = new Clinic[] {clinic};
        } else {
            List<Clinic> clinicList = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getClinics();
            clinics = new Clinic[clinicList.size()];
            clinics = clinicList.toArray(clinics);
        }

        ArrayAdapter<Clinic> adapter = new ArrayAdapter<Clinic>(getActivity(),
                android.R.layout.simple_spinner_item, clinics);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClinic.setAdapter(adapter);
    }

    private void loadPatientsSpinner() throws TrialCatalogException {
        Spinner spinnerPatient = ((Spinner) getView().findViewById(R.id.reading_patient));
        Patient[] patients = null;
        if (action != null && action.equals(getString(R.string.add_reading_patient))) {
            Patient patient;
            patient = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getPatient(reading.getPatientId());
            patients = new Patient[] {patient};
        } else {
            List<Patient> patientList = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getActivePatients();
            patients = new Patient[patientList.size()];
            patients = patientList.toArray(patients);
        }

        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(getActivity(),
                android.R.layout.simple_spinner_item, patients);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(adapter);
    }

    private void loadTypeSpinner() {
        Spinner spinnerType = ((Spinner) getView().findViewById(R.id.reading_type));

        List<String> stringTypes = ReadingFactory.getPrettyReadingTypes();
        String[] types = new String[stringTypes.size()];
        types = stringTypes.toArray(types);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!validate()) {
            if (mListener != null) {
                mListener.onInputError();
            }
        } else {
            if (mListener != null) {
                mListener.onInputOk();
            }
        }
    }

    private boolean validate() {
        boolean answer = false;
        if (presenter != null) {
            if (validate(getReadingId(), MAX_READING_ID, false)) {
                answer = true;
            }
        }
        return answer;
    }

    private boolean validate(String text, int maxLength, boolean allowSpace) {
        boolean answer = false;
        String matchString = allowSpace ? getString(R.string.regex_no_special_chars_allow_spaces)
                : getString(R.string.regex_no_special_chars);

        if (text != null && !text.trim().isEmpty() && text.trim().length() <= maxLength) {
            if (text.matches(matchString)) {
                answer = true;
            }
        }

        return answer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_reading:
                if (mListener != null) {
                    mListener.onSaveClicked();
                }
                break;
            case R.id.reading_value:
                showAndGetValueDialog();
                break;
        }
    }

    private void showAndGetValueDialog() {
        Spinner spinnerType = ((Spinner) getView().findViewById(R.id.reading_type));
        Object selectedItem = spinnerType.getSelectedItem();

        if (selectedItem != null && selectedItem instanceof String) {
            String type = (String)selectedItem;

            switch(type) {
                case ReadingFactory.PRETTY_BLOOD_PRESSURE:
                    DialogFragment dialogFragment = new BloodPressureDialog();
                    dialogFragment.show(getActivity().getFragmentManager(), getString(R.string.tag_blood_pressure));
                    break;
                case ReadingFactory.PRETTY_STEPS:
                    DialogFragment stepsDialog = new StepsDialog();
                    stepsDialog.show(getActivity().getFragmentManager(), getString(R.string.tag_steps));
                    break;
                case ReadingFactory.PRETTY_TEMPERATURE:
                    DialogFragment tempDialog = new TemperatureDialog();
                    tempDialog.show(getActivity().getFragmentManager(), getString(R.string.tag_temperature));
                    break;
                case ReadingFactory.PRETTY_WEIGHT:
                    DialogFragment weightDialog = new WeightDialog();
                    weightDialog.show(getActivity().getFragmentManager(), getString(R.string.tag_weight));
                    break;
            }
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
        return ((TextView)getView().findViewById(R.id.reading_id)).getText().toString();
    }

    @Override
    public String getClinicId() {
        return null;
    }

    @Override
    public String getPatientId() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public LocalDate getDate() {
        return null;
    }

    @Override
    public LocalTime getTime() {
        return null;
    }

    @Override
    public void setReadingId(String id) {

    }

    @Override
    public void setClinicId(String clinicId) {

    }

    @Override
    public void setPatientId(String patientId) {

    }

    @Override
    public void setType(String type) {

    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public void setDate(LocalDate date) {

    }

    @Override
    public void setTime(LocalTime time) {

    }

    @Override
    public void setDateAndTimePickerListeners(View view) {

    }

    @Override
    public void setDisabledSave(boolean disabled) {
        ((Button) getView().findViewById(R.id.save_reading)).setEnabled(!disabled);
    }

//    @Override
//    public String getReadingId() {
//        return ((EditText)getView().findViewById(R.id.reading_id_txt)).getText().toString();
//    }
//
//    @Override
//    public String getClinicId() {
//        return ((EditText)getView().findViewById(R.id.reading_clinic_id_txt)).getText().toString();
//    }
//
//    @Override
//    public String getPatientId() {
//        return ((EditText)getView().findViewById(R.id.reading_patient_id_txt)).getText().toString();
//    }
//
//    @Override
//    public String getType() {
//        return ((Spinner)getView().findViewById(R.id.reading_type_spinner)).getSelectedItem().toString();
//    }
//
//    @Override
//    public String getValue() {
//        return ((EditText)getView().findViewById(R.id.reading_value_txt)).getText().toString();
//    }
//
//    @Override
//    public LocalDate getDate() {
//        String date = ((TextView)getView().findViewById(R.id.reading_date_txt)).getText().toString();
//        LocalDate answer = null;
//
//        if (date != null && !date.isEmpty()) {
//            try {
//                answer = LocalDate.parse(date);
//            } catch (DateTimeParseException ex) {
//
//            }
//        }
//
//        return answer;
//        //return LocalDate.parse(((EditText)getView().findViewById(R.id.reading_date_txt)).getText().toString());
//    }
//
//    @Override
//    public LocalTime getTime() {
//        return LocalTime.parse(((EditText)getView().findViewById(R.id.reading_time_txt)).getText().toString());
//    }
//
//    @Override
//    public void setReadingId(String id) {
//        ((EditText)getView().findViewById(R.id.reading_id_txt)).setText(id);
//    }
//
//    @Override
//    public void setClinicId(String clinicId) {
//        ((EditText)getView().findViewById(R.id.reading_clinic_id_txt)).setText(clinicId);
//    }
//
//    @Override
//    public void setPatientId(String patientId) {
//        ((EditText)getView().findViewById(R.id.reading_patient_id_txt)).setText(patientId);
//    }
//
//    @Override
//    public void setType(String type) {
//        int i = 0;
//        if (type.equals(Reading.class.getName())) {
//            ((Spinner)getView().findViewById(R.id.reading_type_spinner)).setSelection(0);
//            //TODO
//        } else {
//            for (String s : getResources().getStringArray(R.array.reading_types)) {
//                if (type.equals(s)) {
//                    break;
//                } else {
//                    i++;
//                }
//            }
//            ((Spinner)getView().findViewById(R.id.reading_type_spinner)).setSelection(i);
//        }
//    }
//
//    @Override
//    public void setValue(String value) {
//        ((EditText)getView().findViewById(R.id.reading_value_txt)).setText(value);
//    }
//
//    @Override
//    public void setDate(LocalDate date) {
//        ((EditText)getView().findViewById(R.id.reading_date_txt)).setText(date.toString());
//    }
//
//    @Override
//    public void setTime(LocalTime time) {
//        ((EditText)getView().findViewById(R.id.reading_time_txt)).setText(time.toString());
//    }
//
//    @Override
//    public void setDisabledSave(boolean b) {
//
//    }
//
//    @Override
//    public void setDateAndTimePickerListeners(View view) {
//
//    }

    public interface OnFragmentInteractionListener {
        //void onTimeClicked();
        void onSaveClicked();
        void onInputError();
        void onInputOk();
    }
}
