package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialEvent;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ExportingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialException;
import edu.metrostate.ics372.thatgroup.clinicaltrial.importexport.TrialDataExporter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.importexport.TrialDataImportExporterFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.importexport.TrialDataImporter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.models.ClinicalTrialModel;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();
        state.setCurrentActivity(this);

        EnterFilenameDialog efd = new EnterFilenameDialog(this,
                getString(R.string.export_title), getString(R.string.json_extension));

        efd.addFilenameListener(new EnterFilenameDialog.FilenameListener() {
            @Override
            public void filenameEntered(String filename) {
                machine.process((ClinicalTrialEvent.ON_SELECT));

                runOnUiThread(() -> {
                    exportFile(filename);
                });
            }

            @Override
            public void actionCancelled() { machine.process((ClinicalTrialEvent.ON_CANCEL)); }
        });

        efd.showDialog();
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

    private void exportFile(String filename) {
        ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
        TrialDataExporter exporter = null;
        File path = new File(getExternalFilesDir(null)
                + File.separator + getString(R.string.app_name));

        if (!path.exists()) {
            path.mkdirs();
        }

        filename = path.getAbsolutePath() + File.separator + filename;

        try {
            exporter = TrialDataImportExporterFactory.getTrialExporter(filename);

            if (exporter == null) {
                machine.process(ClinicalTrialEvent.ON_IMPORT_FILE_INVALID);
            } else {
                new ExportOperation() {
                    /**
                     * Runs on the UI thread before {@link #doInBackground}.
                     *
                     * @see #onPostExecute
                     * @see #doInBackground
                     */
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        machine.process(ClinicalTrialEvent.ON_EXPORT_BEGIN);
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
                        ExportingState state = (ExportingState)machine.getCurrentState();

                        state.setMessage(s);
                        machine.process(ClinicalTrialEvent.ON_EXPORT_END);
                    }
                }.execute(filename);
            }
        } catch (TrialException e) {
            machine.process(ClinicalTrialEvent.ON_ERROR);
        }
    }

    private class ExportOperation extends AsyncTask<String, Integer, String> {

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
        protected String doInBackground(String... files) {
            String file = files[0];
            String result;
            ClinicalTrialStateMachine machine = ((ClinicalTrialClient)getApplication()).getMachine();
            ClinicalTrialModel model = machine.getApplication().getModel();

            try (OutputStream os = new FileOutputStream(file)){
                List<Patient> patients = model.getPatients();
                List<Clinic> clinics = model.getClinics();
                List<Reading> readings = model.getReadings();
                TrialDataExporter exporter = TrialDataImportExporterFactory.getTrialExporter(file);
                exporter.setPatients(patients);
                exporter.setClinics(clinics);
                exporter.setReadings(readings);

                exporter.write(os);
                result = String.format(Strings.SUCCESS_FILE_IMPORTED_EXPORTED, Strings.MSG_EXPORTED,
                        clinics.size(), patients.size(), readings.size());

                result = getString(R.string.export_file_location) + " " + file + "\n\n" + result;

            } catch (TrialException | IOException e) {
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
