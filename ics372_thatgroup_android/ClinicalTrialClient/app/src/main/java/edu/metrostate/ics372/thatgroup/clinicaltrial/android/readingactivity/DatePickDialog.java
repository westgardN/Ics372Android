package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Calendar;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

public class DatePickDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private ReadingPresenter presenter;
    public DatePickDialog() {
        super();
    }

    void setPresenter(ReadingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        // Create a new instance of DatePickDialog and return it
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month+1, day);
        ((TextView) getActivity().findViewById(R.id.reading_date)).setText(date.toString());

        if (presenter != null) {
            presenter.updateView(false);
        }
    }
}
