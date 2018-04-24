package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ImportingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.MainActivityState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.importexport.TrialDataImportExporterFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.importexport.TrialDataImporter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();
        state.setCurrentActivity(this);

        File mPath = new File(getExternalFilesDir(null) +
                File.separator + getString(R.string.app_name));
        FileDialog fileDialog = new FileDialog(this, mPath, getString(R.string.select_import_file), null);
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());

                machine.process((ClinicalTrialEvent.ON_SELECT));

                runOnUiThread(() -> {
                    importFile(file);
                });
            }

            @Override
            public void actionCancelled() {
                machine.process((ClinicalTrialEvent.ON_CANCEL));
            }
        });

        fileDialog.showDialog();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();

        machine.process(ClinicalTrialEvent.ON_PREVIOUS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void importFile(File file) {
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialModel model = machine.getApplication().getModel();
        TrialDataImporter importer = null;

        try {
            importer = TrialDataImportExporterFactory.getTrialImporter(file.getName());

            if (importer == null) {
                machine.process(ClinicalTrialEvent.ON_IMPORT_FILE_INVALID);
            } else {
                new ImportOperation() {
                    /**
                     * Runs on the UI thread before {@link #doInBackground}.
                     *
                     * @see #onPostExecute
                     * @see #doInBackground
                     */
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        model.setImporting(true);
                        machine.process(ClinicalTrialEvent.ON_IMPORT_BEGIN);
                    }

                    /**
                     * <p>Runs on the UI thread after {@link #doInBackground}. The
                     * specified result is the value returned by {@link #doInBackground}.</p>
                     * <p>
                     * <p>This method won't be invoked if the task was cancelled.</p>
                     *
                     * @param s The result of the operation computed by {@link #doInBackground}.
                     * @see #onPreExecute
                     * @see #doInBackground
                     * @see #onCancelled(Object)
                     */
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        model.setImporting(false);
                        ImportingState state = (ImportingState)machine.getCurrentState();

                        state.setMessage(s);
                        machine.process(ClinicalTrialEvent.ON_IMPORT_END);
                    }
                }.execute(file);
            }
        } catch (TrialException e) {
            machine.process(ClinicalTrialEvent.ON_ERROR);
        }
    }

    private class ImportOperation extends AsyncTask<File, Integer, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param files The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(File... files) {
            File file = files[0];
            String result = Strings.EMPTY;
            try {
                ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
                ClinicalTrialModel model = machine.getApplication().getModel();
                TrialDataImporter importer = TrialDataImportExporterFactory.getTrialImporter(file.getName());

                try (InputStream is = new FileInputStream(file)) {
                    boolean success = importer.read(model.getTrial(), is);
                    if (success) {
                        int readingCount = 0;
                        int patientCount = 0;
                        int clinicCount = 0;
                        List<Reading> readings = importer.getReadings();
                        List<Clinic> clinics = importer.getClinics();
                        List<Patient> patients = importer.getPatients();

                        for (Clinic clinic : clinics) {
                            if (model.getClinic(clinic.getId()) == null) {
                                model.addClinic(clinic.getId(), clinic.getName() == null ? clinic.getId() : clinic.getName());
                                clinicCount++;
                            }
                        }

                        for (Patient patient : patients) {
                            if (model.getPatient(patient.getId()) == null) {
                                model.addPatient(patient);
                                patientCount++;
                            }
                        }

                        for (Reading reading : readings) {
                            if (reading.getClinicId() == null || reading.getClinicId().trim().isEmpty()) {
                                reading.setClinicId(model.getSelectedOrDefaultClinic().getId());
                            }

                            Clinic clinic = model.getClinic(reading.getClinicId());

                            if (clinic == null) {
                                if (model.addClinic(reading.getClinicId(), reading.getClinicId())) {
                                    ++clinicCount;
                                    clinic = model.getClinic(reading.getClinicId());
                                }
                            }

                            Patient patient = model.getPatient(reading.getPatientId());

                            if (patient == null) {
                                if (model.addPatient(reading.getPatientId(), LocalDate.now())) {
                                    ++patientCount;
                                    patient = model.getPatient(reading.getPatientId());
                                }
                            }

                            if (patient != null && clinic != null) {
                                if (model.importReading(reading)) {
                                    readingCount++;
                                }
                            }
                        }
                        final int cCount = clinicCount;
                        final int pCount = patientCount;
                        final int rCount = readingCount;
                        result = String.format(Strings.SUCCESS_FILE_IMPORTED_EXPORTED, Strings.MSG_IMPORTED, cCount, pCount, rCount);
                    } else {
                        result = Strings.ERR_FILE_NOT_IMPORTED;
                    }
                }
            } catch (IOException | TrialException e) {
                result = e.getMessage();
            }

            return result;
        }

        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
