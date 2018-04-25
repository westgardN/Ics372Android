package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import java.util.ArrayList;
import java.util.List;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.PatientStatus;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

/**
 * @author That Group
 */
public abstract class ClinicalTrialState implements State {
    private ClinicalTrialStateMachine machine;
    private Context context;
    private Activity activity;
    private Activity currentActivity;

    /**
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /**
     *
     * @param machine
     * @param context
     */
    public ClinicalTrialState(ClinicalTrialStateMachine machine, Context context) {
        if (machine == null) {
            throw new IllegalArgumentException("machine cannot be null.");
        }

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        }

        this.machine = machine;
        this.context = context;
        this.activity = extractActivity();
    }

    /**
     *
     * @return
     */
    private Activity extractActivity() {
        while (true) {
            if (context instanceof Application) {
                return null;
            } else if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                Context baseContext = ((ContextWrapper) context).getBaseContext();
                // Prevent Stack Overflow.
                if (baseContext == context) {
                    return null;
                }
                context = baseContext;
            } else {
                return null;
            }
        }
    }

    /**
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     *
     * @return
     */
    public Activity getFromActivity() {
        return activity;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName().toString();
    }

    /**
     *
     * @return
     */
    public ClinicalTrialStateMachine getMachine() {
        return machine;
    }

    /**
     *
     * @return
     */
    public boolean canAdd() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canAddReading() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canViewReadings() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canSelect() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canUpdate() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canDelete() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canView() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canImport() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean canExport() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasClinic() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasPatient() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasPatientStatus() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasReading() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean hasReadings() {
        return false;
    }

    /**
     *
     * @return
     */
    public Clinic getClinic() {
        return null;
    }

    /**
     *
     * @return
     */
    public Patient getPatient() {
        return null;
    }

    /**
     *
     * @return
     */
    public PatientStatus getPatientStatus() {
        return null;
    }

    /**
     *
     * @return
     */
    public Reading getReading() {
        return null;
    }

    /**
     *
     * @return
     */
    public List<Reading> getReadings() {
        return new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        String answer = "";

        answer = getClass().getSimpleName();

        return answer;
    }

    /**
     *
     * @param event
     */
    @Override
    public void process(ClinicalTrialEvent event) {  }
}
