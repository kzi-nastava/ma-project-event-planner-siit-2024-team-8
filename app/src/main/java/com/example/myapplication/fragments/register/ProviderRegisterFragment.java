package com.example.myapplication.fragments.register;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.ImageAdapter;
import com.example.myapplication.callbacks.UserRegisterCallBack;
import com.example.myapplication.databinding.FragmentProviderRegisterBinding;
import com.example.myapplication.databinding.FragmentRegisterStepOneBinding;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.Notification;
import com.example.myapplication.domain.dto.user.UserCreateRequest;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderRegisterFragment extends Fragment implements ImageAdapter.OnImageClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGES_REQUEST =  1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserViewModel userVM;

    private List<Uri> selectedImageUris = new ArrayList<>();

    private RecyclerView imagesRecyclerView;
    private ImageAdapter imageAdapter;

    private FragmentProviderRegisterBinding binding;


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
        binding = FragmentProviderRegisterBinding.inflate(inflater,container,false);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding.setUserVM(userVM);
        Button nextButton = binding.registerFinishButton;
        nextButton.setOnClickListener(v -> onFinishButtonClick());

        Button button = binding.insertImageButton2;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_IMAGES_REQUEST);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                selectedImageUris.add(data.getData());
            }
            imageAdapter.notifyDataSetChanged(); // Refresh list
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        imagesRecyclerView = view.findViewById(R.id.imagesRecyclerView);
        imageAdapter = new ImageAdapter(selectedImageUris,this);
        int spanCount = 2;
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        imagesRecyclerView.setAdapter(imageAdapter);
    }

    public void onFinishButtonClick() {
        RegisterFragment parentFragment = (RegisterFragment) getParentFragment();
        //retrieve data and continue if all is ok
        if (!isDataValid()) {
            return;
        }
        userVM.registerProvider(((MainActivity) getActivity()).imageFile, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(getContext(),"Successfuly registered!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        },getContext());
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

    private boolean isDataValid() {
        UserCreateRequest  userCreateRequest = userVM.getUser().getValue();
        assert userCreateRequest != null;
        if (userCreateRequest.getCompanyDescription().isEmpty() || userCreateRequest.getCompanyName().isEmpty()){
            NotificationsUtils.getInstance().showErrToast(getContext(),"Fill all fields!");
            return false;
        }
        try {
            ArrayList<String> imgs = userCreateRequest.getCompanyImagesURL();
            imgs.addAll(selectedImageUris.stream().map(Uri::toString).collect(Collectors.toList()));
            userCreateRequest.setCompanyImagesURL(imgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void pickImage() {
        if (((MainActivity)getActivity()).checkPermission()) {
            //Toast.makeText(getContext(), "Permission already granted!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).openGallery();
        } else {
            ((MainActivity)getActivity()).requestPermission();
        }
    }

    @Override
    public void onRemove(Uri imageUri) {
        int position = selectedImageUris.indexOf(imageUri);
        if (position != -1) {
            selectedImageUris.remove(position);
            imageAdapter.notifyItemRemoved(position);
        }
    }
}