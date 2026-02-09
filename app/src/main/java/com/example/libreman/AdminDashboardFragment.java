package com.example.libreman;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminDashboardFragment extends Fragment {

    public AdminDashboardFragment() {
        super(R.layout.fragment_admin_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView stats = view.findViewById(R.id.rvStats);
        stats.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        stats.setAdapter(new StatsAdapter());
    }
}
