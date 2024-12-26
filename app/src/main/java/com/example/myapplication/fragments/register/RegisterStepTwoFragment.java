package com.example.myapplication.fragments.register;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.domain.dto.UserCreateRequest;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterStepTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterStepTwoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterStepTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterStepTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterStepTwoFragment newInstance(String param1, String param2) {
        RegisterStepTwoFragment fragment = new RegisterStepTwoFragment();
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
        View view =  inflater.inflate(R.layout.fragment_register_step_two, container, false);

        Button nextButton = view.findViewById(R.id.registerNextButton2);
        nextButton.setOnClickListener(v -> onNextButtonClick());

        ((MainActivity)getActivity()).imageView = view.findViewById(R.id.imageView3);
        Button button = view.findViewById(R.id.insertImageButton);

        button.setOnClickListener(v -> pickImage());

        return view;
    }

    public void pickImage() {
        if (((MainActivity)getActivity()).checkPermission()) {
            //Toast.makeText(getContext(), "Permission already granted!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).openGallery();
        } else {
            ((MainActivity)getActivity()).requestPermission();
        }
    }

    private void onNextButtonClick() {
        RegisterFragment parentFragment = (RegisterFragment) getParentFragment();
        if (!retrieveData(parentFragment.userCreateRequest)) {
            Toast.makeText(getContext(), "Something went cataclysmically wrong.", Toast.LENGTH_SHORT).show();
            return;
        }
        //continue if everythink ok
        RegisterStepThreeFragment registerStepThree = new RegisterStepThreeFragment();
        assert parentFragment != null;
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right )
                .replace(R.id.registerLayout, registerStepThree)
                .addToBackStack(null)
                .commit();
        parentFragment.changeTitle(3);
        parentFragment.animateProgressBar(66);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean retrieveData(UserCreateRequest userCreateRequest) {
        try {
            String filePath = getRealPathFromURI(((MainActivity) getActivity()).imageUri);
            ((MainActivity) getActivity()).imageFile = new File(filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = requireActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }
}