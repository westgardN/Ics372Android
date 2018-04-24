package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity;

import java.util.List;
import edu.metrostate.ics372.thatgroup.clinicaltrial.android.BasePresenter;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;

public class ReadingsPresenter implements BasePresenter {
    private ReadingsView view = null;

    private List<Reading> readings = null;

    public void setView(ReadingsView view){ this.view = view; }

    public void setReadings(List<Reading> readings) { this.readings = readings; }

    public void addReading(Reading reading) {
        if (readings != null) {
            readings.add(reading);
        }
    }

    public void updateReading(Reading reading) {
        if (readings != null && readings.contains(reading)) {
            readings.remove(reading);
            readings.add(reading);
        }
    }

    @Override
    public void subscribe(){
        updateView();
    }

    @Override
    public void unsubscribe(){ }

    public void  updateView() {
        if (view != null && readings != null) {
            view.setReadings(readings);
        }
    }
}
