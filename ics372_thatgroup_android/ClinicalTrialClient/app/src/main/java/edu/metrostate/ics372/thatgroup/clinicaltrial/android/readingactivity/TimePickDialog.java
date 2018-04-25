package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.util.Calendar;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

/**
 * Aauthor That Group
 */
public class TimePickDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {
    private ReadingPresenter presenter;

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Get a Calendar instance
        final Calendar calendar = Calendar.getInstance();
        // Get the current hour and minute
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        /*
            Creates a new time picker dialog.
                TimePickDialog(Context context, TimePickDialog.OnTimeSetListener listener,
                    int hourOfDay, int minute, boolean is24HourView)
         */
        // Create a TimePickDialog with current time
        android.app.TimePickerDialog tpd = new android.app.TimePickerDialog(getActivity(),this,hour,minute,false);
        // Return the TimePickDialog
        return tpd;
    }

    void setPresenter(ReadingPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        TextView time = (TextView) getActivity().findViewById(R.id.reading_time);
        time.setText(LocalTime.of(hourOfDay, minute).toString());

        if (presenter != null) {
            presenter.updateView(false);
        }
    }
}