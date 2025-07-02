package com.example.myapplication.fragments.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.callbacks.UserRegisterCallBack;
import com.example.myapplication.databinding.FragmentRegisterStepThreeBinding;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterStepThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterStepThreeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisterStepThreeBinding binding;

    private UserViewModel userViewModel;

    public RegisterStepThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterStepThreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterStepThreeFragment newInstance(String param1, String param2) {
        RegisterStepThreeFragment fragment = new RegisterStepThreeFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentRegisterStepThreeBinding.inflate(inflater,container,false);


        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);


        binding.setUserVM(userViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        Button nextButton = binding.registerNextButton3;
        nextButton.setOnClickListener(v -> onNextButtonClick());

        return binding.getRoot();
    }

    private void onNextButtonClick() {
        RegisterFragment parentFragment = (RegisterFragment) getParentFragment();
        //retrieve data and continue if everything ok:
        if (!retrieveData(parentFragment.userCreateRequest)) {
            Toast.makeText(getContext(), "All fields are required and must be properly filled.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (parentFragment.userCreateRequest.getUserType().equals(Role.PROVIDER)) {
            ProviderRegisterFragment providerRegister = new ProviderRegisterFragment();
            assert parentFragment != null;
            parentFragment.getChildFragmentManager().beginTransaction()
                    .setCustomAnimations( R.anim.enter_from_right,
                            R.anim.exit_to_left,
                            R.anim.enter_from_left,
                            R.anim.exit_to_right )
                    .replace(R.id.registerLayout, providerRegister)
                    .addToBackStack(null)
                    .commit();
            parentFragment.changeTitle(5);
            parentFragment.animateProgressBar(85);
        } else {
            userViewModel.saveUserData(((MainActivity) getActivity()).imageFile, new UserRegisterCallBack() {
                @Override
                public void onSuccess() {
                    NotificationsUtils.getInstance().showSuccessToast(requireContext(), "Successfully registered!");
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

                @Override
                public void onServerError(String errorMessage) {
                    NotificationsUtils.getInstance().showErrToast(requireContext(), errorMessage);
                }

                @Override
                public void onNetworkError(Throwable t) {
                    NotificationsUtils.getInstance().showErrToast(requireContext(),"Network error!");
                }
            });
        }
    }

    private boolean retrieveData(UserCreateRequest userCreateRequest) {
        String email = ((EditText)this.getView().findViewById(R.id.editTextEmail)).getText().toString();
        String password = ((EditText)this.getView().findViewById(R.id.editTextPassword)).getText().toString();
        String passwordConfirm = ((EditText)this.getView().findViewById(R.id.editTextPasswordConfirm)).getText().toString();
        Spinner spinner = this.getView().findViewById(R.id.option_picker_spinner);
        if (email.isBlank() || email.isEmpty() || password.isBlank() || password.isEmpty() || passwordConfirm.isEmpty() || passwordConfirm.isBlank() || !password.equals(passwordConfirm)) {
            return false;
        }
        return true;
    }
}