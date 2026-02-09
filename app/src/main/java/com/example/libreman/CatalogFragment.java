package com.example.libreman;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogFragment extends Fragment {

    public CatalogFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.recyclerBooks);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Grid spacing
        int spacing = (int) (16 * getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(
                new GridSpacingItemDecoration(2, spacing, false)
        );

        recyclerView.setAdapter(new CatalogBookAdapter(getContext()));

        // Back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Notification button (placeholder)
        ImageButton btnNotification = view.findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "New features incoming",
                        Toast.LENGTH_SHORT).show()
        );

        // FAB click
        View fabScan = view.findViewById(R.id.fabScan);
        fabScan.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Scanner coming soon",
                        Toast.LENGTH_SHORT).show()
        );

        return view;
    }
}
