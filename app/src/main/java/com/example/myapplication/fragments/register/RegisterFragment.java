package com.example.myapplication.fragments.register;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRegisterBinding;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.fragments.LoginFragment;
import com.example.myapplication.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected UserCreateRequest userCreateRequest = new UserCreateRequest();

    private UserViewModel userViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        RegisterStepOneFragment stepOneFragment = new RegisterStepOneFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.registerLayout, stepOneFragment)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRegisterBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_register, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        ImageButton button = binding.backButton;
        button.setOnClickListener(v -> onBackButtonClick());
        return binding.getRoot();
    }

    private void onBackButtonClick() {
        Fragment current = getChildFragmentManager().findFragmentById(R.id.registerLayout);
        LoginFragment loginFragment = new LoginFragment();
        if (current instanceof RegisterStepOneFragment){
            getParentFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, loginFragment)
                    .commit();
        }else if (current instanceof RegisterStepTwoFragment){
            RegisterStepOneFragment stepOneFragment = new RegisterStepOneFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.registerLayout, stepOneFragment)
                    .commit();
            animateProgressBar(0);
            changeTitle(1);
        }else if (current instanceof RegisterStepThreeFragment){
            RegisterStepTwoFragment stepTwoFragment = new RegisterStepTwoFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.registerLayout, stepTwoFragment)
                    .commit();
            animateProgressBar(33);
            changeTitle(2);
        }
    }

    public void animateProgressBar(int progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progressBar != null) {
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
            progressAnimator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
            progressAnimator.setDuration(500); // Duration of 2 seconds
            progressAnimator.start();
        }
    }

    public void changeTitle(int step){
        TextView title = getView().findViewById(R.id.stepTitleTextView);
        if (step == 1){
            title.setText("Basic Info");
        }else if (step == 2){
            title.setText("Profile Picture");
        }else if(step == 3){
            title.setText("Login Info");
        }else if (step==5) {
            title.setText("Provider Register");
        }else if (step==4){
            title.setText("Finished");
            ImageButton back = getView().findViewById(R.id.backButton);
            back.setVisibility(View.GONE);
        }
    }
}