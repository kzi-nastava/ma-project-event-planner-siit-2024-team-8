package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.domain.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderRegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProviderRegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderRegisterFragment newInstance(String param1, String param2) {
        ProviderRegisterFragment fragment = new ProviderRegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_register, container, false);

        Button nextButton = view.findViewById(R.id.registerFinishButton);
        nextButton.setOnClickListener(v -> onFinishButtonClick());

        ((MainActivity)getActivity()).imageView = view.findViewById(R.id.imageView4);
        Button button = view.findViewById(R.id.insertImageButton2);

        button.setOnClickListener(v -> pickImage());

        return view;
    }

    public void onFinishButtonClick() {
        RegisterFragment parentFragment = (RegisterFragment) getParentFragment();
        //retrieve data and continue if all is ok
        if (!retrieveData(parentFragment.user)) {
            Toast.makeText(getContext(), "Company name is required.", Toast.LENGTH_SHORT).show();
            return;
        }
        RegisterFinalStep finalStep = new RegisterFinalStep();
        assert parentFragment != null;
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right)
                .replace(R.id.registerLayout, finalStep)
                .addToBackStack(null)
                .commit();
        parentFragment.changeTitle(4);
        parentFragment.animateProgressBar(100);
    }

    public void pickImage() {
        if (((MainActivity)getActivity()).checkPermission()) {
            //Toast.makeText(getContext(), "Permission already granted!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).openGallery();
        } else {
            ((MainActivity)getActivity()).requestPermission();
        }
    }

    private boolean retrieveData(User user) {
        String companyName = ((EditText)this.getView().findViewById(R.id.editTextCompanyName)).getText().toString();
        String companyDesc = ((EditText)this.getView().findViewById(R.id.editTextCompanyDesc)).getText().toString();
        //String initialImage = ((EditText)this.getView().findViewById(R.id.editTextAddress)).getText().toString();
        if (companyName.isBlank() || companyName.isEmpty()) {
            return false;
        }
        user.setCompanyName(companyName);
        user.setCompanyDescription(companyDesc);
        try {
            ArrayList<String> imgs = user.getCompanyImagesURL();
            imgs.add(((MainActivity) getActivity()).imageUri.toString());
            user.setCompanyImagesURL(imgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}