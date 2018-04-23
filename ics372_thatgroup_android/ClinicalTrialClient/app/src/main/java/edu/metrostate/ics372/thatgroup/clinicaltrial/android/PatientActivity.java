package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientErrorState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.PatientState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.PatientStatus;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

public class PatientActivity extends AppCompatActivity implements PatientFragment.OnFragmentInteractionListener {
    private ClinicalTrialStateMachine machine;
    private PatientPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState current = (ClinicalTrialState) machine.getCurrentState();
        current.setCurrentActivity(this);
        presenter = new PatientPresenter(machine);

        Intent intent = getIntent();
        Patient patient = null;

        if (intent.hasExtra(getResources().getString(R.string.intent_update_patient))) {
            Object obj = intent.getSerializableExtra(getResources().getString(R.string.intent_update_patient));

            if (obj instanceof Patient) {
                patient = (Patient) obj;
            }
        }

        if (patient == null) {
            patient = new Patient();
        }

        presenter.setPatient(patient);
        PatientFragment fragment = PatientFragment.newInstance(patient);
        fragment.setPresenter(presenter);

        getFragmentManager().beginTransaction().add(R.id.fragment_patient, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        final ClinicalTrialStateMachine machine =
                ((ClinicalTrialClient)getApplication()).getMachine();
        machine.process(ClinicalTrialEvent.ON_CANCEL);
    }

    @Override
    public void onSaveClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_OK);
    }

    @Override
    public void onStartTrialClicked() {
        Patient patient = presenter.getPatient();

        if (patient != null) {
            getTrialDateAndStatus(patient, true);
        }
    }

    @Override
    public void onEndTrialClicked() {
        Patient patient = presenter.getPatient();

        if (patient != null) {
            getTrialDateAndStatus(patient, false);
        }
    }

    private void getTrialDateAndStatus(Patient patient, boolean start) {
        LocalDate date = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year, int monthOfYear, int dayOfMonth)  -> {
                LocalDate ld = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                if (!start) {
                    if (isDateOnOrAfter(ld, patient.getTrialStartDate()) && isDateOnOrBefore(ld, LocalDate.now())) {
                        patient.setTrialEndDate(LocalDate.from(ld));
                        getPatientStatus(patient);
                    } else {
                        if (ld != null) {
                            Toast.makeText(
                                    machine.getApplication(), Strings.ERR_DATE_MSG,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (isDateOnOrBefore(ld, LocalDate.now())) {
                        patient.setTrialStartDate(LocalDate.from(ld));
                        patient.setTrialEndDate(null);
                        patient.setStatusId(PatientStatus.ACTIVE_ID);
                    } else {
                        if (ld != null) {
                            Toast.makeText(
                                    machine.getApplication(), Strings.ERR_DATE_MSG,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                PatientState state = (PatientState)machine.getCurrentState();
                state.setPatient(patient);
                presenter.setPatient(patient);
                presenter.updateView();
            }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        datePickerDialog.show();
    }

    private void getPatientStatus(Patient patient) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.patient_status_title));
        List<PatientStatus> types = null;
        try {
            types = machine.getApplication().getModel().getPatientStatuses();
        } catch (TrialCatalogException e) {

        }
        if (types != null) {
            final String[] real = new String[types.size()];

            for (int i = 0; i < types.size(); i++) {
                real[i] = types.get(i).getId();
            }

            b.setItems(real, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    patient.setStatusId(real[which]);
                    PatientState state = (PatientState)machine.getCurrentState();
                    state.setPatient(patient);
                    presenter.setPatient(patient);
                    presenter.updateView();
                    dialog.dismiss();
                }

            });

            b.setCancelable(false);
            b.show();
        }
    }

    private boolean isDateOnOrAfter(LocalDate ldA, LocalDate ldB) {
        boolean answer = false;

        if (ldA != null && ldB != null) {
            if (ldA.isEqual(ldB) || ldA.isAfter(ldB)) {
                answer = true;
            }
        } else if (ldA != null && ldB == null) {
            answer = true;
        }

        return answer;
    }

    private boolean isDateOnOrBefore(LocalDate ldA, LocalDate ldB) {
        boolean answer = false;

        if (ldA != null && ldB != null) {
            if (ldA.isEqual(ldB) || ldA.isBefore(ldB)) {
                answer = true;
            }
        } else if (ldA != null && ldB == null) {
            answer = true;
        }

        return answer;
    }

    @Override
    public void onViewReadingsClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_VIEW_READINGS);
    }

    @Override
    public void onAddReadingClicked() {
        PatientState state = (PatientState)machine.getCurrentState();

        state.setPatient(presenter.getPatient());
        machine.process(ClinicalTrialEvent.ON_ADD_READING);
    }

    @Override
    public void onInputError() {
        machine.process(ClinicalTrialEvent.ON_ERROR);
        presenter.updateView(false);
    }

    @Override
    public void onInputOk() {
        if (machine.getCurrentState() instanceof PatientErrorState) {
            machine.process(ClinicalTrialEvent.ON_OK);
        }
        presenter.updateView(false);
    }
}
