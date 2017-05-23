package net.villim.villim;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PasscodeFragment extends Fragment {

    private MainActivity activity;

    public PasscodeFragment() {
        // Required empty public constructor
    }

    public static PasscodeFragment newInstance() {
        PasscodeFragment fragment = new PasscodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View passcodeView = inflater.inflate(R.layout.fragment_passcode, container, false);

        activity = ((MainActivity) getActivity());

        // Set bottom button text.
        String leftBottomButtonText = getString(R.string.passcode_change_cancel);
        activity.setBottomButtonText(true, leftBottomButtonText);
        String rightBottomButtonText = getString(R.string.passcode_change_done);
        activity.setBottomButtonText(false, rightBottomButtonText);

        // Register left button listener.
        activity.registerBottomButtonListener(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss fragment.
                getActivity().onBackPressed();
            }
        });
        // Register right button listener.
        activity.registerBottomButtonListener(false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss fragment.
                getActivity().onBackPressed();
            }
        });

        activity.showBottomButtons(true, true);

        return passcodeView;
    }

}
