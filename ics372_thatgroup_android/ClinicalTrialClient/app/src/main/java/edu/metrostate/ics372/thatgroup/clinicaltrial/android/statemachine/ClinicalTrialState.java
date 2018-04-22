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

public abstract class ClinicalTrialState implements State {
    private ClinicalTrialStateMachine machine;
    private Context context;
    private Activity activity;
    private Activity currentActivity;


    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

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

    public Context getContext() {
        return context;
    }

    public Activity getFromActivity() {
        return activity;
    }



    @Override
    public String toString() {
        return this.getClass().getSimpleName().toString();
    }

    public ClinicalTrialStateMachine getMachine() {
        return machine;
    }

    public boolean canAdd() {
        return false;
    }

    public boolean canAddReading() {
        return false;
    }

    public boolean canViewReadings() {
        return false;
    }

    public boolean canSelect() {
        return false;
    }

    public boolean canUpdate() {
        return false;
    }

    public boolean canDelete() {
        return false;
    }

    public boolean canView() {
        return false;
    }

    public boolean canImport() {
        return false;
    }

    public boolean canExport() {
        return false;
    }

    public boolean hasClinic() {
        return false;
    }

    public boolean hasPatient() {
        return false;
    }

    public boolean hasPatientStatus() {
        return false;
    }

    public boolean hasReading() {
        return false;
    }

    public boolean hasReadings() {
        return false;
    }

    public Clinic getClinic() {
        return null;
    }

    public Patient getPatient() {
        return null;
    }

    public PatientStatus getPatientStatus() {
        return null;
    }

    public Reading getReading() {
        return null;
    }

    public List<Reading> getReadings() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        String answer = "";

        answer = getClass().getSimpleName();

        return answer;
    }

    @Override
    public void process(ClinicalTrialEvent event) {  }
}
