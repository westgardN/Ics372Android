package edu.metrostate.ics372.thatgroup.clinicaltrial.android.clinicsactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.State;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ClinicsState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClinicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClinicsFragment extends Fragment implements ClinicsView,
        AdapterView.OnItemClickListener, Button.OnClickListener {
    private ClinicsPresenter presenter;
    private OnFragmentInteractionListener mListener;

    public ClinicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClinicsFragment.
     */
    public static ClinicsFragment newInstance() {
        ClinicsFragment fragment = new ClinicsFragment();
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinics, container, false);
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.subscribe();
        }

        final ListView viewClinics = ((ListView)getView().findViewById(R.id.clinics));
        viewClinics.setOnItemClickListener(this);

        final Button add = ((Button)getView().findViewById(R.id.add));
        add.setOnClickListener(this);
    }

    /**
     *
     * @param context
     */
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

    /**
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        presenter = null;
    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getActivity().getApplication()).getMachine();

        machine.process(ClinicalTrialEvent.ON_ADD);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getActivity().getApplication()).getMachine();
        Object obj = parent.getAdapter().getItem(position);

        if (obj instanceof Clinic) {
            Clinic clinic = (Clinic) obj;
            State state = machine.getCurrentState();

            if (state instanceof ClinicsState) {
                ClinicsState clinicsState = (ClinicsState)state;

                if (clinicsState != null && clinic != null) {
                    clinicsState.setClinic(clinic);
                    machine.process(ClinicalTrialEvent.ON_SELECT);
                }
            }
        }
    }

    /**
     *
     * @param clinics
     */
    @Override
    public void setClinics(List<Clinic> clinics) {
        ArrayAdapter<Clinic> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, clinics);
        ((ListView)getView().findViewById(R.id.clinics)).setAdapter(arrayAdapter);
    }

    /**
     *
     * @param presenter
     */
    @Override
    public void setPresenter(ClinicsPresenter presenter) {
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
