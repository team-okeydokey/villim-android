package net.villim.villim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_FULLNAME;

/**
 * Created by seongmin on 6/17/17.
 */

public class CancelVisitDialog extends DialogFragment {

    static CancelVisitDialog.CancelVisitDialogListener listener;

    public interface CancelVisitDialogListener {
        public void onCancelConfirm(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (VisitDetailActivity) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_cancel_visit, null);

        /* User name */
        TextView logoutConfirmText = (TextView) dialogView.findViewById(R.id.logout_confirm_text);
        logoutConfirmText.setText(getString(R.string.cancel_visit_confirm));

        /* Confirm logout button */
        Button confirmButton = (Button) dialogView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelConfirm(CancelVisitDialog.this);
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
