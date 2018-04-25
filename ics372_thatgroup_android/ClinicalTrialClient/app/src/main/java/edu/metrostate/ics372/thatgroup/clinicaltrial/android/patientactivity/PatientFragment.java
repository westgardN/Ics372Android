package edu.metrostate.ics372.thatgroup.clinicaltrial.android.patientactivity;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment implements PatientView,
        Button.OnClickListener, TextWatcher {
    private static final int MAX_PATIENT_ID = 32;
    private static final String ARG_PATIENT = "patient";

    private PatientPresenter presenter;
    private Patient patient;

    private OnFragmentInteractionListener mListener;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param patient the patient we are working with
     * @return A new instance of fragment PatientFragment.
     */
    public static PatientFragment newInstance(Patient patient) {
        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATIENT, patient);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.subscribe();
        }

        ((Button)getView().findViewById(R.id.start_trial)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.end_trial)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.add_reading)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.view_readings)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.save_patient)).setOnClickListener(this);

        TextView id = (TextView)getView().findViewById(R.id.patient_id);

        id.addTextChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Object obj = getArguments().getSerializable(ARG_PATIENT);
            if (obj instanceof Patient) {
                this.patient = (Patient) obj;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_trial:
                if (mListener != null) {
                    mListener.onStartTrialClicked();
                }
                break;
            case R.id.end_trial:
                if (mListener != null) {
                    mListener.onEndTrialClicked();
                }
                break;
            case R.id.save_patient:
                if (mListener != null) {
                    mListener.onSaveClicked();
                }
                break;
            case R.id.view_readings:
                if (mListener != null) {
                    mListener.onViewReadingsClicked();
                }
                break;
            case R.id.add_reading:
                if (mListener != null) {
                    mListener.onAddReadingClicked();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
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

    @Override
    public void setPatientId(String id) {
        ((TextView)getView().findViewById(R.id.patient_id)).setText(id);
    }

    @Override
    public void setStartDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String strDate = "";

        if (date != null) {
            strDate = date.format(formatter);
        }

        ((TextView)getView().findViewById(R.id.patient_start_date)).setText(strDate);
    }

    @Override
    public void setEndDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String strDate = "";

        if (date != null) {
            strDate = date.format(formatter);
        }

        ((TextView)getView().findViewById(R.id.patient_end_date)).setText(strDate);
    }

    @Override
    public void setStatusId(String statusId) {
        ((TextView)getView().findViewById(R.id.patient_status)).setText(statusId);
    }

    @Override
    public String getPatientId() {
        return ((TextView)getView().findViewById(R.id.patient_id)).getText().toString();
    }

    @Override
    public LocalDate getStartDate() {
        String date = ((TextView)getView().findViewById(R.id.patient_start_date)).getText().toString();
        LocalDate answer = null;

        if (date != null && !date.isEmpty()) {
            try {
                answer = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException ex) {

            }
        }

        return answer;
    }

    @Override
    public LocalDate getEndDate() {
        String date = ((TextView)getView().findViewById(R.id.patient_end_date)).getText().toString();
        LocalDate answer = null;

        if (date != null && !date.isEmpty()) {
            try {
                answer = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException ex) {

            }
        }

        return answer;
    }

    @Override
    public String getStatusId() {
        return ((TextView)getView().findViewById(R.id.patient_status)).getText().toString();
    }

    @Override
    public void setVisibleStartTrial(boolean visible) {
        ((Button)getView().findViewById(R.id.start_trial)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setVisibleEndTrial(boolean visible) {
        ((Button)getView().findViewById(R.id.end_trial)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setVisibleAddReading(boolean visible) {
        ((Button)getView().findViewById(R.id.add_reading)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setVisibleViewReadings(boolean visible) {
        ((Button)getView().findViewById(R.id.view_readings)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setVisibleSave(boolean visible) {
        ((Button)getView().findViewById(R.id.save_patient)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setDisabledSave(boolean disabled) {
        ((Button)getView().findViewById(R.id.save_patient)).setEnabled(!disabled);
    }

    @Override
    public void setDisabledId(boolean disabled) {
        ((TextView)getView().findViewById(R.id.patient_id)).setInputType(disabled ?
                InputType.TYPE_NULL : InputType.TYPE_CLASS_TEXT);

        if (disabled) {
            ((TextView)getView().findViewById(R.id.patient_id)).setFocusable(false);
        }
    }

    @Override
    public void setDisabledStartTrial(boolean disabled) {
        ((Button)getView().findViewById(R.id.start_trial)).setEnabled(!disabled);
    }

    @Override
    public void setDisabledEndTrial(boolean disabled) {
        ((Button)getView().findViewById(R.id.end_trial)).setEnabled(!disabled);
    }

    @Override
    public void setPresenter(PatientPresenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    private boolean validate() {
        boolean answer = false;
        if (presenter != null) {
            if (validate(getPatientId(), MAX_PATIENT_ID, false)) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onSaveClicked();

        void onStartTrialClicked();

        void onEndTrialClicked();

        void onViewReadingsClicked();

        void onAddReadingClicked();

        void onInputError();

        void onInputOk();
    }
}
