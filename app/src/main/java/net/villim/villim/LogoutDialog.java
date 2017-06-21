package net.villim.villim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_FULLNAME;
/**
 * Created by seongmin on 6/17/17.
 */
public class LogoutDialog extends DialogFragment {

    static LogoutDialogListener listener;

    public interface LogoutDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public static LogoutDialog newInstance(LogoutDialogListener callerFragment, String name) {
        LogoutDialog frag = new LogoutDialog();

        listener = callerFragment;

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(KEY_FULLNAME, name);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);

        /* User name */
        TextView logoutConfirmText = (TextView) dialogView.findViewById(R.id.logout_confirm_text);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString(KEY_FULLNAME);
            String logoutConfirmString = String.format(getString(R.string.logout_confirm_format),name);
            logoutConfirmText.setText(logoutConfirmString);
        }

        /* Confirm logout button */
        Button confirmButton = (Button) dialogView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogPositiveClick(LogoutDialog.this);
            }
        });

        /* Cancel button */
        Button closeButton = (Button) dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(dialogView);

        return builder.create();
    }
}
