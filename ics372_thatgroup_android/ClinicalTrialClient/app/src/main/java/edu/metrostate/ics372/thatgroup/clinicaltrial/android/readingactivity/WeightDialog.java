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

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.R;

/**
 * @author That Group
 */
public class WeightDialog extends DialogFragment {
    private ReadingPresenter presenter;

    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";
    public static final String MESSAGE = "Enter Weight";

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
        View view  = inflater.inflate(R.layout.dialog_weight, null);

        EditText weight = view.findViewById(R.id.weight);
        EditText weightType = view.findViewById(R.id.weight_type);
        // inflate and set the layout for the dialog
        // pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setMessage(MESSAGE)
                // action buttons
                .setPositiveButton(OK, (dialog, id) -> {
                        ((TextView) getActivity().findViewById(R.id.reading_value))
                                .setText(String.format("%s %s", weight.getText().toString(), weightType.getText().toString()));

                    if (presenter != null) {
                        presenter.updateView(false);
                    }
                });

                builder.setNegativeButton(CANCEL, (dialog, id) -> {
                    // remove the dialog from the screen
                });
        return builder.create();
    }

    void setPresenter(ReadingPresenter presenter) {
        this.presenter = presenter;
    }

}
