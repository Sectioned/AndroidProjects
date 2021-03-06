package com.bgrummitt.compscisimpleapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.bgrummitt.compscisimpleapp.R;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //Set dialog to have title and message and button to acknowledge dialog but nothing else
        builder.setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_button_ok_text, null);

        return builder.create();
    }

}
