package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingsactivity.presenter;
import java.util.Locale;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Reading;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.ReadingFactory;

public class ReadingPresenter {
    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private Reading reading;
    private String id;
    private String clinicId;
    private String patient_id;
    private String type;
    private String value;
    private String date;
    private String time;

    public ReadingPresenter(Reading reading) {
      this.reading = reading;
      id = reading.getId();
      clinicId = reading.getClinicId();
      patient_id = reading.getPatientId();
      type = ReadingFactory.getPrettyReadingType(reading);
      value = reading.getValue().toString();
      date = reading.getDate().toLocalDate().toString();
      time = String.format(
              Locale.getDefault(),"%d:%d:%d",
              reading.getDate().getHour(),
              reading.getDate().getMinute(),
              reading.getDate().getSecond()
      );
    }

    public Reading getReading() {
        return reading;
    }

    public String getId() {
        return id;
    }

    public String getClinicId() {
        return clinicId;
    }

    public String getPatientId() {
        return patient_id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
