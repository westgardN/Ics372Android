package edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine;

public enum ClinicalTrialEvent implements Event {
    ON_IMPORT("onImport"),
    ON_IMPORT_ERROR("onImportError"),
    ON_IMPORT_OK("onImportOk"),
    ON_IMPORT_CANCEL("onImportCancel"),
    ON_EXPORT("onExport"),
    ON_EXPORT_ERROR("onExportError"),
    ON_EXPORT_OK("onExportOk"),
    ON_EXPORT_CANCEL("onExportCancel"),
    ON_CLINICS("onClinics"),
    ON_CLINIC_ERROR("onClinicError"),
    ON_CLINIC_OK("onClinicOk"),
    ON_CLINIC_CANCEL("onClinicCancel"),
    ON_CLINIC_ADD("onClinicAdd"),
    ON_CLINIC_SELECT("onClinicSelect"),
    ON_CLINIC_VIEW("onClinicView"),
    ON_CLINIC_UPDATE("OnClinicUpdate"),
    ON_PATIENTS("onPatients"),
    ON_PATIENT_ERROR("onPatientError"),
    ON_PATIENT_OK("onPatientOk"),
    ON_PATIENT_CANCEL("onPatientCancel"),
    ON_PATIENT_ADD("onPatientAdd"),
    ON_PATIENT_SELECT("onPatientSelect"),
    ON_PATIENT_VIEW("onPatientView"),
    ON_PATIENT_UPDATE("OnPatientUpdate"),
    ON_READINGS("onReadings"),
    ON_READING_ERROR("onReadingError"),
    ON_READING_OK("onReadingOk"),
    ON_READING_CANCEL("onReadingCancel"),
    ON_READING_ADD("onReadingAdd"),
    ON_READING_SELECT("onReadingSelect"),
    ON_READING_VIEW("onReadingView"),
    ON_READING_UPDATE("onReadingUpdate"),
    ON_START("onStart"),
    ON_EXIT("onExit");

    private final String name;

    private ClinicalTrialEvent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
