package edu.metrostate.ics372.thatgroup.clinicaltrial.android.readingactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

/**
 * @author That Group
 */
public class BloodPressureDialog extends DialogFragment {
    private ReadingPresenter presenter;

    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";
    public static final String MESSAGE = "Enter Blood Pressure";

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialog_blood_press, null);

        EditText sys = view.findViewById(R.id.systolic);
        EditText dia = view.findViewById(R.id.diastolic);
        // inflate and set the layout for the dialog
        // pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setMessage(MESSAGE)
                // action buttons
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    /**
                     *
                     * @param dialog
                     * @param id
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((TextView) getActivity().findViewById(R.id.reading_value)).setText(
                                String.format("%s/%s", sys.getText(), dia.getText())
                        );

                        if (presenter != null) {
                            presenter.updateView(false);
                        }

                    }
                })
                .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                    /**
                     *
                     * @param dialog
                     * @param id
                     */
                    public void onClick(DialogInterface dialog, int id) {
                        // remove the dialog from the screen
                    }
                });
        return builder.create();
    }

    void setPresenter(ReadingPresenter presenter) {
        this.presenter = presenter;
    }

}