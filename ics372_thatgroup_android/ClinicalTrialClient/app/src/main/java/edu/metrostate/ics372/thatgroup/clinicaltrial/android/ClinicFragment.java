package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClinicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClinicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClinicFragment extends Fragment implements ClinicView,
        Button.OnClickListener {
    private static final int TEXT_NONE = 0;
    private static final int TEXT_TEXT = 1;

    private ClinicPresenter presenter;
    private static final String ARG_CLINIC = "clinic";

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
    // TODO: Rename and change types and number of parameters
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
        presenter.subscribe();

        ((Button)getView().findViewById(R.id.view_readings)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.add_reading)).setOnClickListener(this);
        ((Button)getView().findViewById(R.id.save_clinic)).setOnClickListener(this);
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
    public void setDisabledAddReading(boolean disabled) {
        ((Button)getView().findViewById(R.id.add_reading)).setEnabled(!disabled);
    }

    @Override
    public void setDisabledViewReadings(boolean disabled) {
        ((Button)getView().findViewById(R.id.view_readings)).setEnabled(!disabled);
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
    }
}
