package com.example.myapplication.fragments.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRegisterStepOneBinding;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterStepOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterStepOneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserViewModel userViewModel;
    private FragmentRegisterStepOneBinding binding;

    public RegisterStepOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterStepOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterStepOneFragment newInstance(String param1, String param2) {
        RegisterStepOneFragment fragment = new RegisterStepOneFragment();
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
        binding = FragmentRegisterStepOneBinding.inflate(inflater,container,false);


        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.setUserVM(userViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        Button nextButton = binding.registerNextButton3;
        nextButton.setOnClickListener(v -> nextButtonClicked());

        return binding.getRoot();
    }


    private void nextButtonClicked() {
        RegisterFragment parentFragment = (RegisterFragment) getParentFragment();
        //begin user creation
        if (!retrieveData(parentFragment.userCreateRequest)) {
            Toast.makeText(getContext(), "First and last name are required.", Toast.LENGTH_SHORT).show();
            return;
        }
        //next step if everything ok
        RegisterStepTwoFragment stepTwoFragment = new RegisterStepTwoFragment();
        assert parentFragment != null;
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right )
                .replace(R.id.registerLayout, stepTwoFragment)
                .addToBackStack(null)
                .commit();
        parentFragment.changeTitle(2);
        parentFragment.animateProgressBar(33);
    }

    //persforms automatic verification as well
    private boolean retrieveData(UserCreateRequest userCreateRequest) {
        String firstName = ((EditText)this.getView().findViewById(R.id.editTextFirstName)).getText().toString();
        String lastName = ((EditText)this.getView().findViewById(R.id.editTextLastName)).getText().toString();
        String address = ((EditText)this.getView().findViewById(R.id.editTextAddress)).getText().toString();
        String number = ((EditText)this.getView().findViewById(R.id.editTextNumber)).getText().toString();
        if (firstName.isBlank() || firstName.isEmpty() || lastName.isEmpty() || lastName.isBlank()) {
            return false;
        }
        return true;
    }
}