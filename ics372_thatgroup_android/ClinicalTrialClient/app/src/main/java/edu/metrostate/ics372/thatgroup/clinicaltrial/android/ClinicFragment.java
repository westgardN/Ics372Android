package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ClinicFragment extends Fragment implements ClinicView {
    ClinicPresenter presenter;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public void setId(String id) {
        ((TextView)getView().findViewById(R.id.clinic_id)).setText(id);
    }

    @Override
    public void setName(String name) {
        ((TextView)getView().findViewById(R.id.clinic_name)).setText(name);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
