package com.example.eventplanner.fragments;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.eventplanner.R;
import com.example.eventplanner.adapters.AssetCardAdapter;
import com.example.eventplanner.adapters.EventCardAdapter;
import com.example.eventplanner.domain.AssetDTO;
import com.example.eventplanner.domain.AssetType;
import com.example.eventplanner.domain.EventDTO;
import com.example.eventplanner.domain.OfferingType;

import java.sql.Date;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView eventRecyclerView;
    private RecyclerView assetRecyclerView;

    public UserHomeFragment() {
        // Required empty public constructor
    }
    public UserHomeFragment(RecyclerView recyclerView){
        this.eventRecyclerView = recyclerView;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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
        View root =  inflater.inflate(R.layout.fragment_user_home, container, false);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);
        EventCardAdapter eventCardAdapter = new EventCardAdapter(getContext());  // Use getContext() for context in fragments
        eventCardAdapter.SetOnClick(getActivity(),getActivity().getSupportFragmentManager());
        eventCardAdapter.set_eventCards(new ArrayList<EventDTO>(createEvents().stream().limit(5).collect(Collectors.toList())));
        eventRecyclerView.setAdapter(eventCardAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));

        assetRecyclerView = view.findViewById(R.id.topAssetRecyclerView);
        AssetCardAdapter adapter = new AssetCardAdapter(getContext());
        adapter.SetOnClick(getActivity(),getActivity().getSupportFragmentManager());
        adapter.setAssets(new ArrayList<>(createAssets().stream().limit(5).collect(Collectors.toList())));
        assetRecyclerView.setAdapter(adapter);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    public static ArrayList<EventDTO> createEvents(){
        ArrayList<EventDTO> eventCards = new ArrayList<>();
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        eventCards.add(new EventDTO("Exit", new Date(2024, 7, 10), new Date(2024, 7, 14), "https://w7.pngwing.com/pngs/830/906/png-transparent-computer-icons-android-logo-android-logo-black-silhouette.png"));
        return eventCards;
    }

    public static ArrayList<AssetDTO> createAssets(){
        ArrayList<AssetDTO> assetCards = new ArrayList<>();
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        assetCards.add(new AssetDTO("Baloons", AssetType.PRODUCT,"https://www.balloonsdirect.com/media/catalog/product/cache/37d07aa740105e301b528c2cfe995ae1/b/l/black_lg_2.jpg"));
        assetCards.add(new AssetDTO("Sound System Service",AssetType.SERVICE,"https://www.netplanet.rs/images/dynacord.png"));
        return assetCards;
    }


}