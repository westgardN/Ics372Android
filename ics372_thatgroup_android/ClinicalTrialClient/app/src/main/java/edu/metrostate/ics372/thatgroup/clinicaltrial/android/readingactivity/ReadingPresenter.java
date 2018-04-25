package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.widget.Switch;

import java.time.LocalDateTime;
import java.time.LocalTime;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.ClinicalTrialStateMachine;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.statemachine.states.ReadingState;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.BloodPressure;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.UnitValue;
import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

/**
 * @author That Group
 */
public class ReadingPresenter implements BasePresenter {
    private ClinicalTrialStateMachine machine;

    private ReadingView view = null;

    private Reading reading = null;

    /**
     *
     * @param machine
     */
    public ReadingPresenter(ClinicalTrialStateMachine machine) {
        this.machine = machine;
    }

    /**
     *
     * @param view
     */
    public void setView(ReadingView view){ this.view = view; }

    /**
     *
     * @param reading
     */
    public void setReading(Reading reading) { this.reading = reading; }

    /**
     *
     * @return
     */
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

    /**
     *
     */
    @Override
    public void subscribe(){ updateView(); }

    /**
     *
     */
    @Override
    public void unsubscribe(){ }

    /**
     *
     */
    public void  updateView() {
        updateView(true);
    }

    /**
     *
     * @param setData
     */
    public void  updateView(boolean setData) {
        if (view != null && reading != null) {
            ClinicalTrialState state = (ClinicalTrialState) machine.getCurrentState();

            if (setData) {
                view.setReadingId(reading.getId());
                view.setClinicId(reading.getClinicId());
                view.setPatientId(reading.getPatientId());
                view.setType(ReadingFactory.getPrettyReadingType(reading));

                setReadingValue();

                LocalDateTime ldt = reading.getDate();

                if (ldt != null) {
                    view.setDate(ldt.toLocalDate());
                    view.setTime(ldt.toLocalTime());
                }

                view.setDisabledSave(false);
                view.setDisabledId(state.canUpdate());
            } else {
//                view.setDisabledSave(!(state.canUpdate() || state.canAdd()));
                view.setDisabledId(state.canUpdate());
            }
        }
    }


    private void setReadingValue() {
        Object readingValue = reading.getValue();
        String answer = Strings.EMPTY;

        if (readingValue != null) {
            String type = ReadingFactory.getReadingType(reading);

            switch(type) {
                case ReadingFactory.BLOOD_PRESSURE:
                    if (readingValue instanceof BloodPressure.BloodPressureValue) {
                        BloodPressure.BloodPressureValue bValue = (BloodPressure.BloodPressureValue) readingValue;
                        if (bValue.getDiastolic() >= 0) {
                            answer = bValue.toString();
                        }
                    }
                    break;
                case ReadingFactory.STEPS:
                    if (readingValue instanceof Integer) {
                        Integer sValue = (Integer) readingValue;
                        if (sValue >= 0) {
                            answer = "" + sValue;
                        }
                    }
                    break;
                case ReadingFactory.TEMPERATURE:
                    if (readingValue instanceof UnitValue) {
                        UnitValue tValue = (UnitValue) readingValue;
                        if (tValue.getNumberValue() instanceof Double) {
                            double dVal = (Double) tValue.getNumberValue();
                            if (!Double.isNaN(dVal)) {
                                answer = "" + tValue.toString();
                            }
                        }
                    }
                    break;
                case ReadingFactory.WEIGHT:
                    if (readingValue instanceof UnitValue) {
                        UnitValue tValue = (UnitValue) readingValue;
                        if (tValue.getNumberValue() instanceof Long) {
                            long lVal = (Long) tValue.getNumberValue();
                            if (lVal >= 0) {
                                answer = "" + tValue.toString();
                            }
                        }
                    }
                    break;
            }
        }

        view.setValue(answer);
    }
}
