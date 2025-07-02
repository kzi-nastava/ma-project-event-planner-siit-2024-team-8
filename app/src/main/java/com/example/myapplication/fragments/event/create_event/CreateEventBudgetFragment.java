package com.example.myapplication.fragments.event.create_event;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.BudgetItemAdapter;
import com.example.myapplication.databinding.FragmentCreateEventBinding;
import com.example.myapplication.databinding.FragmentCreateEventBudgetBinding;
import com.example.myapplication.databinding.FragmentCreateEventTypeBinding;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.EventType;
import com.example.myapplication.domain.dto.BudgetItemCreateRequest;
import com.example.myapplication.fragments.HomePageFragment;
import com.example.myapplication.services.EventService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.AssetCategoryViewModel;
import com.example.myapplication.viewmodels.EventViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventBudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventBudgetFragment extends Fragment implements BudgetItemAdapter.OnNewBudgetItemClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EventViewModel eventViewModel;

    private FragmentCreateEventBudgetBinding binding;

    private List<BudgetItem> items;

    private BudgetItemAdapter adapter;

    public CreateEventBudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventBudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventBudgetFragment newInstance(String param1, String param2) {
        CreateEventBudgetFragment fragment = new CreateEventBudgetFragment();
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
        binding =  FragmentCreateEventBudgetBinding.inflate(inflater,container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        items = new ArrayList<>();
        adapter = new BudgetItemAdapter(items,this);
        RecyclerView itemsRecyclerView = binding.itemsRecyclerView;
        binding.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerView.setAdapter(adapter);

        Button next = binding.createEventNextBudgetButton;
        next.setOnClickListener(v -> {
            onNextClicked();
        });

        Button add = binding.addItemButton;
        add.setOnClickListener(v ->
        {
            onNewBudgetItemClick();
        });

        Button suggested = binding.suggestedBudgetButton;
        suggested.setOnClickListener(v -> {
            onSuggestedButtonClick();
        });

        return binding.getRoot();
    }

    private void onSuggestedButtonClick() {
        EventType eventType = eventViewModel.getCreateEventRequest().getValue().getEventType();
        for (AssetCategory category : eventType.getAssetCategories()){
            adapter.addItem(new BudgetItem(category));
        }
    }

    private void onNextClicked() {
        if (!isDataValid()){
            return;
        }
        List<BudgetItemCreateRequest> budgetItems = adapter.getBudgetItems().stream()
                                                           .map(BudgetItemCreateRequest::new)
                                                           .collect(Collectors.toList());
        eventViewModel.getCreateEventRequest().getValue().setBudgetItems(budgetItems);
        eventViewModel.getCreateEventRequest().getValue().setOrganizerID(JwtTokenUtil.getUserId());
        EventService eventService = new EventService();
        eventService.createEvent(eventViewModel.getCreateEventRequest().getValue(),
                new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() || response.body() != null){
                            NotificationsUtils.getInstance().showSuccessToast(getContext(), "Succesfully created an Event!");
                            getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getParentFragmentManager().beginTransaction().replace(R.id.main, new HomePageFragment()).commit();

                        }else{
                            NotificationsUtils.getInstance().showErrToast(getContext(), response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.e("EventService", "API call failed", t);
                    }
                });
    }

    private boolean isDataValid() {
        for (BudgetItem item : adapter.getBudgetItems()){
            if (item.getPlannedAmount() == 0.0 || item.getPlannedAmount() == null){
                NotificationsUtils.getInstance().showErrToast(requireContext(),"Planned ammount of every item is required and must be positive number!");
                return false;
            }
            if (item.getCategory() == null){
                NotificationsUtils.getInstance().showErrToast(requireContext(),"Asset Category can't be null!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onNewBudgetItemClick() {
        adapter.addItem(new BudgetItem());
    }
}