package net.villim.villim;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import static net.villim.villim.ProfileViewActivity.EMAIL;

/**
 * Created by seongmin on 6/17/17.
 */
public class CurrencyPreferenceDialog extends DialogFragment{

    public static final String TITLE = "title";

    static currencyPreferenceDialogListener listener;

    public interface currencyPreferenceDialogListener {
        public void onCurrencyPicked(DialogFragment dialog, int code);
    }

    public static CurrencyPreferenceDialog newInstance(String title) {
        CurrencyPreferenceDialog frag = new CurrencyPreferenceDialog();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        String title = getArguments().getString(TITLE);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_listview, null);

        /* Dialog title */
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        /* Cancel button */
        Button closeButton = (Button) dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /* Listview */
        ListView listView = (ListView) dialogView.findViewById(R.id.list_view);
        listView.setAdapter(ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.currencies_full,
                R.layout.listview_dialog_item));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onCurrencyPicked(CurrencyPreferenceDialog.this, position);
            }
        });
        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (SettingsActivity) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
