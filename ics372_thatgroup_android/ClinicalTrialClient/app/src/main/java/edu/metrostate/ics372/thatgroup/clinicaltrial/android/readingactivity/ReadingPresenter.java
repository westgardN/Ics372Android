package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

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
            reading = ReadingFactory.getReading(view.getType());

            reading.setId(view.getReadingId());
            reading.setClinicId(view.getClinicId());
            reading.setPatientId(view.getPatientId());
            String value = view.getValue();
            if (value != null && !value.isEmpty()) {
                reading.setValue(value);
            }
            if (view.getDate() != null && view.getTime() != null) {
                reading.setDate(LocalDateTime.of(view.getDate(), view.getTime()));
            }
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
//                view.setValue(Integer.valueOf(reading.getValue().toString()) == 0 ? Strings.EMPTY : reading.getValue().toString());
//                view.setDate(reading.getDate().toLocalDate());
//                view.setTime(LocalTime.of(reading.getDate().getHour(), reading.getDate().getMinute()));

                view.setDisabledSave(true);
            } else {
                if (state instanceof ReadingState) {
                    ((ReadingState) state).setReading(getReading());
                }
                view.setDisabledSave(!(state.canUpdate() || state.canAdd()));
            }
        }
    }
}
