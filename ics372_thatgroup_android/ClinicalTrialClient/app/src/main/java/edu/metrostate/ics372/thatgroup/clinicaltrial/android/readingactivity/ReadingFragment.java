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
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

public class ReadingFragment extends Fragment implements ReadingView,
        Button.OnClickListener, TextWatcher {

    private static final int MAX_READING_ID = 32;
    private static final String ARG_READING = "reading";
    private static final String ARG_ACTION = "action";
    private boolean firstTime = true;
    private ReadingPresenter presenter;

    private Reading reading;
    private String action;
    private ArrayAdapter<Clinic> clinicAdapter;
    private ArrayAdapter<Patient> patientAdapter;
    private ArrayAdapter<String> typeAdapter;

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
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
            DatePickDialog dialogFragment = new DatePickDialog();
            dialogFragment.setPresenter(presenter);
            dialogFragment.show(getActivity().getFragmentManager(), getString(R.string.tag_date));
        });

        time.setOnClickListener(v -> {
            TimePickDialog dialogFragment = new TimePickDialog();
            dialogFragment.setPresenter(presenter);
            dialogFragment.show(getActivity().getFragmentManager(), getString(R.string.tag_time));
        });

        if (presenter != null) {
            presenter.subscribe();
        }
    }

    private void loadClinicsSpinner() throws TrialCatalogException {
        Spinner spinnerClinic = ((Spinner) getView().findViewById(R.id.reading_clinic));
        Clinic[] clinics = null;
        if (action != null &&
                (action.equals(getString(R.string.add_reading_clinic)) ||
                        action.equals(getString(R.string.update_reading)))) {
            Clinic clinic;
            clinic = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getClinic(reading.getClinicId());
            clinics = new Clinic[] {clinic};
        } else {
            List<Clinic> clinicList = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getClinics();
            clinics = new Clinic[clinicList.size()];
            clinics = clinicList.toArray(clinics);
        }

        clinicAdapter = new ArrayAdapter<Clinic>(getActivity(),
                android.R.layout.simple_spinner_item, clinics);
        clinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClinic.setAdapter(clinicAdapter);

        spinnerClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (presenter != null) {
                    presenter.updateView(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (presenter != null) {
                    presenter.updateView(false);
                }
            }
        });
    }

    private void loadPatientsSpinner() throws TrialCatalogException {
        Spinner spinnerPatient = ((Spinner) getView().findViewById(R.id.reading_patient));
        Patient[] patients = null;
        if (action != null &&
                (action.equals(getString(R.string.add_reading_patient)) ||
                        action.equals(getString(R.string.update_reading)))) {
            Patient patient;
            patient = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getPatient(reading.getPatientId());
            patients = new Patient[] {patient};
        } else {
            List<Patient> patientList = ((ClinicalTrialClient)getActivity().getApplication()).getModel().getActivePatients();
            patients = new Patient[patientList.size()];
            patients = patientList.toArray(patients);
        }

        patientAdapter = new ArrayAdapter<Patient>(getActivity(),
                android.R.layout.simple_spinner_item, patients);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(patientAdapter);

        spinnerPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (presenter != null) {
                    presenter.updateView(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (presenter != null) {
                    presenter.updateView(false);
                }
            }
        });
    }

    private void loadTypeSpinner() {
        Spinner spinnerType = ((Spinner) getView().findViewById(R.id.reading_type));

        List<String> stringTypes = ReadingFactory.getPrettyReadingTypes();
        String[] types = new String[stringTypes.size()];
        types = stringTypes.toArray(types);

        typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!firstTime) {
                    ((EditText) getView().findViewById(R.id.reading_value)).setText(Strings.EMPTY);
                    showAndGetValueDialog();
                } else {
                    firstTime = false;
                }

                if (presenter != null) {
                    presenter.updateView(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (presenter != null) {
                    presenter.updateView(false);
                }
            }
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
                ClinicalTrialClient app = (ClinicalTrialClient) getActivity().getApplication();
                ClinicalTrialStateMachine machine = app.getMachine();
                ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

                if (state instanceof ReadingState && presenter != null) {
                    ((ReadingState) state).setReading(presenter.getReading());
                    if (state.canAdd() || state.canUpdate()) {
                        if (mListener != null) {
                            mListener.onSaveClicked();
                        }
                    } else {
                        Toast.makeText(
                            getActivity().getApplicationContext(),
                            getActivity().getResources().getString(R.string.err_reading_fill_out_id),
                            Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.reading_value:
                showAndGetValueDialog();
                break;
            default:
                if (presenter != null) {
                    presenter.updateView(false);
                }
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
            if (presenter != null) {
                presenter.updateView(false);
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
    public void setDisabledSave(boolean disabled) {
        ((Button) getView().findViewById(R.id.save_reading)).setEnabled(!disabled);
    }

    @Override
    public String getReadingId() {
        return ((TextView)getView().findViewById(R.id.reading_id)).getText().toString();
    }

    @Override
    public String getClinicId() {
        Object obj = ((Spinner)getView().findViewById(R.id.reading_clinic)).getSelectedItem();
        String answer = Strings.EMPTY;

        if (obj instanceof Clinic) {
            answer = ((Clinic)obj).getId();
        }

        return answer;
    }

    @Override
    public String getPatientId() {
        Object obj = ((Spinner)getView().findViewById(R.id.reading_patient)).getSelectedItem();
        String answer = Strings.EMPTY;

        if (obj instanceof Patient) {
            answer = ((Patient)obj).getId();
        }

        return answer;
    }

    @Override
    public String getType() {
        Object obj = ((Spinner)getView().findViewById(R.id.reading_type)).getSelectedItem();
        String answer = Strings.EMPTY;

        if (obj instanceof String) {
            answer = obj.toString();
        }

        return answer;
    }

    @Override
    public String getValue() {
        return ((EditText)getView().findViewById(R.id.reading_value)).getText().toString();
    }

    @Override
    public LocalDate getDate() {
        String date = ((TextView)getView().findViewById(R.id.reading_date)).getText().toString();
        LocalDate answer = null;

        if (date != null && !date.isEmpty()) {
            try {
                answer = LocalDate.parse(date);
            } catch (DateTimeParseException ex) {

            }
        }

        return answer;
    }

    @Override
    public LocalTime getTime() {
        String time = ((TextView)getView().findViewById(R.id.reading_time)).getText().toString();
        LocalTime answer = null;

        if (time != null && !time.isEmpty()) {
            try {
                answer = LocalTime.parse(time);
            } catch (DateTimeParseException ex) {

            }
        }

        return answer;
    }

    @Override
    public void setReadingId(String id) {
        ((EditText)getView().findViewById(R.id.reading_id)).setText(id);
    }

    @Override
    public void setClinicId(String clinicId) {
        if (clinicId != null && !clinicId.isEmpty()) {
            try {
                Clinic clinic = ((ClinicalTrialClient) getActivity().getApplication()).getModel().getClinic(clinicId);

                int pos = clinicAdapter.getPosition(clinic);

                if (pos >= 0) {
                    ((Spinner) getView().findViewById(R.id.reading_clinic)).setSelection(pos);
                }
            } catch (TrialCatalogException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setPatientId(String patientId) {
        if (patientId != null && !patientId.isEmpty()) {
            try {
                Patient patient = ((ClinicalTrialClient) getActivity().getApplication()).getModel().getPatient(patientId);

                int pos = patientAdapter.getPosition(patient);

                if (pos >= 0) {
                    ((Spinner) getView().findViewById(R.id.reading_patient)).setSelection(pos);
                }
            } catch (TrialCatalogException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setType(String type) {
        if (type != null && !type.isEmpty()) {
            int pos = typeAdapter.getPosition(type);

            if (pos >= 0) {
                ((Spinner) getView().findViewById(R.id.reading_type)).setSelection(pos);
            }
        }
    }

    @Override
    public void setValue(String value) {
        ((EditText)getView().findViewById(R.id.reading_value)).setText(value);
    }

    @Override
    public void setDate(LocalDate date) {
        ((EditText)getView().findViewById(R.id.reading_date)).setText(date != null ? date.toString() : Strings.EMPTY);
    }

    @Override
    public void setTime(LocalTime time) {
        ((EditText)getView().findViewById(R.id.reading_time)).setText(time != null ? time.toString() : Strings.EMPTY);
    }

    public interface OnFragmentInteractionListener {
        void onSaveClicked();
        void onInputError();
        void onInputOk();
    }
}
