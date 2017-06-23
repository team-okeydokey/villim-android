package net.villim.villim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static net.villim.villim.ProfileViewActivity.EMAIL;

/**
 * Created by seongmin on 6/22/17.
 */


public class SimpleEditTextDialog extends DialogFragment  {

    public static final String TITLE = "title";
    public static final String DATA_TYPE = "data_type";
    public static final String DATA_NAME = "data_name";
    public static final String INITIAL_DATA = "initial_data";

    static SimpleEditTextDialog.SimpleEditTextDialogListener listener;

    public interface SimpleEditTextDialogListener {
        public void onConfirm(DialogFragment dialog, int dataType, String newData);
    }

    public static SimpleEditTextDialog newInstance(String title, int dataType, String dataName, String initialData) {
        SimpleEditTextDialog frag = new SimpleEditTextDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putInt(DATA_TYPE, dataType);
        args.putString(DATA_NAME, dataName);
        args.putString(INITIAL_DATA, initialData);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ProfileViewActivity) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String title = getArguments().getString(TITLE);
        final int dataType = getArguments().getInt(DATA_TYPE);
        String dataName = getArguments().getString(DATA_NAME);
        String initialData = getArguments().getString(INITIAL_DATA);


        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_simple_edit_text, null);

        /* Dialog title */
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        /* Field name */
        TextView fieldName = (TextView) dialogView.findViewById(R.id.field_name);
        fieldName.setText(dataName);

        /* Field edit text */
        final TextView fieldContent = (EditText) dialogView.findViewById(R.id.field_content);
        fieldContent.setHint(initialData);
        if (dataType == EMAIL) {
            fieldContent.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        /* Confirm logout button */
        Button confirmButton = (Button) dialogView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm(SimpleEditTextDialog.this, dataType, fieldContent.getText().toString().trim());
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