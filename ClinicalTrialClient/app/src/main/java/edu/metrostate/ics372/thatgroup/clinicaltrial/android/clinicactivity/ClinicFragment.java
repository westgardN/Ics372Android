package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicactivity;

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

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClinicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClinicFragment extends Fragment implements ClinicView,
        Button.OnClickListener, TextWatcher {
    private static final int MAX_CLINIC_ID = 32;
    private static final int MAX_CLINIC_NAME = 255;
    private static final String ARG_CLINIC = "clinic";

    private ClinicPresenter presenter;

    private Clinic clinic;

    private OnFragmentInteractionListener mListener;

    public ClinicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param clinic Parameter 1.
     * @return A new instance of fragment ClinicFragment.
     */
    public static ClinicFragment newInstance(Clinic clinic) {
        ClinicFragment fragment = new ClinicFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLINIC, clinic);
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

        ((Button)getView().findViewById(R.id.view_readings)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.add_reading)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.save_clinic)).setOnClickListener(this);

        TextView id = (TextView)getView().findViewById(R.id.clinic_id);
        TextView name = (TextView)getView().findViewById(R.id.clinic_name);

        id.addTextChangedListener(this);
        name.addTextChangedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Object obj = getArguments().getSerializable(ARG_CLINIC);
            if (obj instanceof Clinic) {
                this.clinic = (Clinic) obj;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinic, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_clinic:
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

    @Override
    public void setClinicId(String id) {
        ((TextView)getView().findViewById(R.id.clinic_id)).setText(id);
    }

    @Override
    public void setClinicName(String name) {
        ((TextView)getView().findViewById(R.id.clinic_name)).setText(name);
    }

    @Override
    public String getClinicId() {
        return ((TextView)getView().findViewById(R.id.clinic_id)).getText().toString();
    }

    @Override
    public String getClinicName() {
        return ((TextView)getView().findViewById(R.id.clinic_name)).getText().toString();
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
        ((Button)getView().findViewById(R.id.save_clinic)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void setDisabledSave(boolean disabled) {
        ((Button)getView().findViewById(R.id.save_clinic)).setEnabled(!disabled);
    }

    @Override
    public void setDisabledId(boolean disabled) {
        ((TextView)getView().findViewById(R.id.clinic_id)).setInputType(disabled ?
                InputType.TYPE_NULL : InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void setDisabledName(boolean disabled) {
        ((TextView)getView().findViewById(R.id.clinic_name)).setInputType(disabled ?
                InputType.TYPE_NULL : InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void setPresenter(ClinicPresenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
    }

    private boolean validate() {
        boolean answer = false;
        if (presenter != null) {
            if (validate(getClinicId(), MAX_CLINIC_ID, false) &&
                    validate(getClinicName(), MAX_CLINIC_NAME, true)) {
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

        void onViewReadingsClicked();

        void onAddReadingClicked();

        void onInputError();

        void onInputOk();
    }
}
