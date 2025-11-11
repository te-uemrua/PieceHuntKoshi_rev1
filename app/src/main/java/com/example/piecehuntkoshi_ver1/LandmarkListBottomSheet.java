package com.example.piecehuntkoshi_ver1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

public class LandmarkListBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_LANDMARKS = "landmarks";
    private ArrayList<Landmark> landmarks;
    private LandmarkAdapter adapter;

    public static LandmarkListBottomSheet newInstance(ArrayList<Landmark> landmarks) {
        LandmarkListBottomSheet fragment = new LandmarkListBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LANDMARKS, landmarks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            landmarks = (ArrayList<Landmark>) getArguments().getSerializable(ARG_LANDMARKS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_landmarks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.landmarks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LandmarkAdapter(landmarks);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public LandmarkAdapter getAdapter() {
        return adapter;
    }
}