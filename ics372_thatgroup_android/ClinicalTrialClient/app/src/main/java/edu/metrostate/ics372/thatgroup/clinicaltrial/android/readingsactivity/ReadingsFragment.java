package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

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
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingsState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClinicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingsFragment extends Fragment implements ReadingsView,
        AdapterView.OnItemClickListener, Button.OnClickListener {
    private ReadingsPresenter presenter;
    private OnFragmentInteractionListener mListener;

    public ReadingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClinicsFragment.
     */
    public static ReadingsFragment newInstance() {
        ReadingsFragment fragment = new ReadingsFragment();
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
        return inflater.inflate(R.layout.fragment_readings, container, false);
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

        final ListView viewReadings = ((ListView)getView().findViewById(R.id.readings));
        viewReadings.setOnItemClickListener(this);

        final Button add = ((Button)getView().findViewById(R.id.add_reading));
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

        if (obj instanceof Reading) {
            Reading reading = (Reading) obj;
            State state = machine.getCurrentState();

            if (state instanceof ReadingsState) {
                ReadingsState readingsState = (ReadingsState) state;

                if (readingsState != null && reading != null) {
                    readingsState.setObject(reading);
                    machine.process(ClinicalTrialEvent.ON_SELECT);
                }
            }
        }
    }

    /**
     *
     * @param readings
     */
    @Override
    public void setReadings(List<Reading> readings) {
        ArrayAdapter<Reading> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, readings);
        ((ListView)getView().findViewById(R.id.readings)).setAdapter(arrayAdapter);
    }

    /**
     *
     * @param visible
     */
    @Override
    public void setVisibleAddReading(boolean visible) {
        ((Button)getView().findViewById(R.id.add_reading)).setVisibility(visible ?
                View.VISIBLE : View.GONE);
    }

    /**
     *
     * @param presenter
     */
    @Override
    public void setPresenter(ReadingsPresenter presenter) {
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
