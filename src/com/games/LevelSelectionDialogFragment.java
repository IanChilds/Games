package com.games;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Binnie
 * Date: 15/03/13
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class LevelSelectionDialogFragment extends DialogFragment {

    static final String SAVED_LEVEL = "savedLevel";
    public int selectedPosition = 0;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedPosition = ((PlayerMenu)getActivity()).getCurrentPosition();
        if (savedInstanceState != null) selectedPosition = savedInstanceState.getInt(SAVED_LEVEL);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        builder.setTitle("Choose Level");
        builder.setSingleChoiceItems(R.array.levels, selectedPosition, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            }
        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mListener.onDialogPositiveClick(LevelSelectionDialogFragment.this, selectedPosition);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(LevelSelectionDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(SAVED_LEVEL, selectedPosition);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /* The activity that creates an instance of this dialog fragment must
* implement this interface in order to receive event callbacks.
* Each method passes the DialogFragment in case the host needs to query it. */
    public interface LevelSelectionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int level);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    LevelSelectionDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the LevelSelectionDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LevelSelectionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement LevelSelectionDialogListener");
        }
    }
}

