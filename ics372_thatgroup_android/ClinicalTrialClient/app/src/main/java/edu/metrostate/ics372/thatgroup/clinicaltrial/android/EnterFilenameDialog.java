package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import edu.metrostate.ics372.thatgroup.clinicaltrial.resources.Strings;

/**
 * @author That Group
 */
public class EnterFilenameDialog {
    private final Activity activity;
    private String title;
    private String extToAdd;
    private FilenameListener listener;

    /**
     *
     */
    public interface FilenameListener {
        void filenameEntered(String filename);

        void actionCancelled();
    }

    /**
     *
     * @param activity
     * @param title
     * @param extToAdd
     */
    public EnterFilenameDialog(Activity activity, String title, String extToAdd) {
        this.activity = activity;
        this.title = title;
        this.extToAdd = extToAdd;
        listener = null;
    }

    /**
     *
     * @param listener
     */
    public void addFilenameListener(FilenameListener listener) {
        this.listener = listener;
    }

    /**
     *
     * @return
     */
    public Dialog createFilenameDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);

        final EditText input = new EditText(activity);
        input.setMaxLines(1);
        input.setSingleLine();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(activity.getResources().getString(R.string.main_export),
                new DialogInterface.OnClickListener() {
                    /**
                     *
                     * @param dialog
                     * @param which
                     */
            public void onClick(DialogInterface dialog, int which) {
                String result = input.getText().toString();

                if (validate(result)) {
                    if (!result.endsWith(extToAdd)) {
                        result += extToAdd;
                    }

                    final String real = result;
                    activity.runOnUiThread(() -> fireFilenameEnteredEvent(real));
                } else {
                    activity.runOnUiThread(() -> {
                        Toast.makeText(
                                activity.getApplicationContext(),
                                Strings.SPECIAL_CHAR_MSG,
                                Toast.LENGTH_LONG).show();
                        showDialog();
                    });
                }
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

    private boolean validate(String filename) {
        boolean answer = false;
            if (validate(filename, true)) {
                answer = true;
            }

        return answer;
    }

    private boolean validate(String text, boolean allowSpace) {
        boolean answer = false;
        String matchString = allowSpace ? activity.getString(R.string.regex_no_special_chars_allow_spaces)
                : activity.getString(R.string.regex_no_special_chars);

        if (text != null && !text.trim().isEmpty()) {
            if (text.matches(matchString)) {
                answer = true;
            }
        }

        return answer;
    }

}
