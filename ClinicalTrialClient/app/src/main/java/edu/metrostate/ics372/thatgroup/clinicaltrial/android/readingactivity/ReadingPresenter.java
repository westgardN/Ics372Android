package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;

public class ReadingPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private ReadingView view = null;

    private Reading reading = null;

    public ReadingPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    public void setView(ReadingView view){ this.view = view; }

    public void setReading(Reading reading) { this.reading = reading; }

    public Reading getReading() {
        if (view != null) {
            if (reading == null) {
                reading = ReadingFactory.getReading(ReadingFactory.PRETTY_WEIGHT);
            }

            reading.setId(view.getReadingId());
            reading.setClinicId(view.getClinicId());
            reading.setPatientId(view.getPatientId());
            reading.setValue(view.getValue());
            reading.setDate(LocalDateTime.of(view.getDate(), view.getTime()));
        }
        return reading;
    }

    @Override
    public void subscribe(){ updateView(); }

    @Override
    public void unsubscribe(){ }

    public void  updateView() {
        updateView(true);
    }

    public void  updateView(boolean setData) {
        if (view != null && reading != null) {
            ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

            if (setData) {
                view.setReadingId(reading.getId());
                view.setClinicId(reading.getClinicId());
                view.setPatientId(reading.getPatientId());
                view.setType(ReadingFactory.getPrettyReadingType(reading));
                view.setValue(reading.getValue() == null ? ReadingFactory.PRETTY_WEIGHT : reading.getValue().toString());
                view.setDate(reading.getDate() == null ? LocalDate.now() : reading.getDate().toLocalDate());
                view.setTime(reading.getDate() == null ? LocalTime.now() : LocalTime.of(reading.getDate().getHour(), reading.getDate().getMinute()));
            } else {
                view.setDisabledSave(!(state.canUpdate() || state.canAdd()));
            }
        }
    }
}
