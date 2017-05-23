package net.villim.villim;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PasscodeFragment extends Fragment {

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

        // Set bottom button text.
        String bottomButtonText = getActivity().getString(R.string.passcode_change_done);
        ((MainActivity)getActivity()).setBottomButton(true, bottomButtonText);

        return passcodeView;
    }

}
