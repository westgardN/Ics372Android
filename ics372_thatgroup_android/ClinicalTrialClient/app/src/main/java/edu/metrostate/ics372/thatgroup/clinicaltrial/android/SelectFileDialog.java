package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author That Gropu
 */
public class SelectFileDialog {
    private static final String PARENT_DIR = "..";
    private static final String DEFAULT_TITLE = "Select a file";
    private final String TAG = getClass().getName();
    private String[] fileList;
    private File currentPath;

    /**
     *
     */
    public interface FileSelectedListener {
        void fileSelected(File file);

        void actionCancelled();
    }

    /**
     *
     */
    public interface DirectorySelectedListener {
        void directorySelected(File directory);

        void actionCancelled();
    }
    private ListenerList<FileSelectedListener> fileListenerList = new ListenerList<FileSelectedListener>();
    private ListenerList<DirectorySelectedListener> dirListenerList = new ListenerList<DirectorySelectedListener>();

    private final Activity activity;
    private boolean selectDirectoryOption;
    private String title;
    private String fileEndsWith;

    /**
     * @param activity
     * @param initialPath
     */
    public SelectFileDialog(Activity activity, File initialPath) {
        this(activity, initialPath, DEFAULT_TITLE, null);
    }

    /**
     *
     * @param activity
     * @param initialPath
     * @param title
     * @param fileEndsWith
     */
    public SelectFileDialog(Activity activity, File initialPath, String title, String fileEndsWith) {
        this.activity = activity;

        setFileEndsWith(fileEndsWith);

        if (!initialPath.exists()) {
            initialPath.mkdirs();
        }

        if (!initialPath.exists()) {
            initialPath = activity.getExternalFilesDir(null);
        }

        this.title = title;

        loadFileList(initialPath);
    }

    /**
     * @return file dialog
     */
    public Dialog createFileDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption) {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, currentPath.getPath());
                    fireDirectorySelectedEvent(currentPath);
                }
            });
        }

        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            /**
             *
             * @param dialog
             * @param which
             */
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    loadFileList(chosenFile);
                    dialog.dismiss();
                    showDialog();
                } else fireFileSelectedEvent(chosenFile);
            }
        });

        builder.setOnCancelListener((di) -> {
            fireFileSelectedCancelledEvent();
        });

        dialog = builder.show();
        return dialog;
    }

    /**
     *
     * @param listener
     */
    public void addFileListener(FileSelectedListener listener) {
        fileListenerList.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeFileListener(FileSelectedListener listener) {
        fileListenerList.remove(listener);
    }

    /**
     *
     * @param selectDirectoryOption
     */
    public void setSelectDirectoryOption(boolean selectDirectoryOption) {
        this.selectDirectoryOption = selectDirectoryOption;
    }

    /**
     *
     * @param listener
     */
    public void addDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.add(listener);
    }

    /**
     *
     * @param listener
     */
    public void removeDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.remove(listener);
    }

    /**
     * Show file dialog
     */
    public void showDialog() {
        createFileDialog().show();
    }

    private void fireFileSelectedEvent(final File file) {
        fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>() {
            public void fireEvent(FileSelectedListener listener) {
                listener.fileSelected(file);
            }
        });
    }

    private void fireFileSelectedCancelledEvent() {
        fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>() {
            public void fireEvent(FileSelectedListener listener) {
                listener.actionCancelled();
            }
        });
    }

    private void fireDirectorySelectedEvent(final File directory) {
        dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>() {
            public void fireEvent(DirectorySelectedListener listener) {
                listener.directorySelected(directory);
            }
        });
    }

    private void fireDirectorySelectedCancelledEvent() {
        dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>() {
            public void fireEvent(DirectorySelectedListener listener) {
                listener.actionCancelled();
            }
        });
    }

    private void loadFileList(File path) {
        this.currentPath = path;
        List<String> r = new ArrayList<String>();
        if (path.exists()) {
            if (path.getParentFile() != null) {
                r.add(PARENT_DIR);
            }

            FilenameFilter filter = new FilenameFilter() {
                /**
                 *
                 * @param dir
                 * @param filename
                 * @return
                 */
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);

                    if (!sel.canRead()) {
                        return false;
                    }

                    if (selectDirectoryOption) {
                        return sel.isDirectory();
                    }
                    else {
                        boolean endsWith = fileEndsWith != null ? filename.toLowerCase().endsWith(fileEndsWith) : true;
                        return endsWith || sel.isDirectory();
                    }
                }
            };
            String[] fileList1 = path.list(filter);
            if (fileList1 != null) {
                for (String file : fileList1) {
                    r.add(file);
                }
            }
        }
        fileList = (String[]) r.toArray(new String[]{});
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
        else return new File(currentPath, fileChosen);
    }

    private void setFileEndsWith(String fileEndsWith) {
        this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : fileEndsWith;
    }

    static class ListenerList<L> {
        private List<L> listenerList = new ArrayList<L>();

        /**
         *
         * @param <L>
         */
        public interface FireHandler<L> {
            void fireEvent(L listener);
        }

        /**
         *
         * @param listener
         */
        public void add(L listener) {
            listenerList.add(listener);
        }

        /**
         *
         * @param fireHandler
         */
        public void fireEvent(FireHandler<L> fireHandler) {
            List<L> copy = new ArrayList<L>(listenerList);
            for (L l : copy) {
                fireHandler.fireEvent(l);
            }
        }

        /**
         *
         * @param listener
         */
        public void remove(L listener) {
            listenerList.remove(listener);
        }

        /**
         *
         * @return
         */
        public List<L> getListenerList() {
            return listenerList;
        }
    }
}

