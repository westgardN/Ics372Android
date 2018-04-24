package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;

public class EnterFilenameDialog {
    private final Activity activity;
    private String title;
    private String extToAdd;
    private FilenameListener listener;

    public interface FilenameListener {
        void filenameEntered(String filename);

        void actionCancelled();
    }

    public EnterFilenameDialog(Activity activity, String title, String extToAdd) {
        this.activity = activity;
        this.title = title;
        this.extToAdd = extToAdd;
        listener = null;
    }

    public void addFilenameListener(FilenameListener listener) {
        this.listener = listener;
    }

    public Dialog createFilenameDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(activity.getResources().getString(R.string.main_export),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String result = input.getText().toString();

                if (!result.endsWith(extToAdd)) {
                    result += extToAdd;
                }

                final String real = result;
                activity.runOnUiThread(() -> fireFilenameEnteredEvent(real));
            }
        });

        builder.setOnCancelListener((di) -> {
            activity.runOnUiThread(() -> fireFilenameCancelledEvent());
        });

        dialog = builder.show();
        return dialog;
    }

    /**
     * Show file dialog
     */
    public void showDialog() {
        createFilenameDialog().show();
    }

    private void fireFilenameEnteredEvent(final String filename) {
        if (listener != null) {
            listener.filenameEntered(filename);
        }
    }

    private void fireFilenameCancelledEvent() {
        if (listener != null) {
            listener.actionCancelled();
        }
    }

}
