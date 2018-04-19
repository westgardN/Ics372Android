package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;

public class ReadingPresenter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Reading reading;
    private String clinicId;
    private String patient_id;
    private String type;
    private String value;
    private String dateTime;

    public ReadingPresenter(Reading reading) {
      this.reading = reading;
      clinicId = reading.getClinicId();
      patient_id = reading.getPatientId();
      type = ReadingFactory.getPrettyReadingType(reading);
      value = reading.getValue().toString();
      dateTime = LocalDateTime.parse(reading.getDate().toString(), formatter).format(formatter);
    }

    public Reading getReading() {
        return reading;
    }

    public String getClinicId() {
        return clinicId;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getDateTime() {
        return dateTime;
    }
}
