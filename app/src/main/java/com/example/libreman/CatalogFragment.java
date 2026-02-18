package com.example.libreman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerBooks);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        CatalogBookAdapter adapter = new CatalogBookAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Tabs
        TextView tabAll = view.findViewById(R.id.tabAll);
        TextView tabAvailable = view.findViewById(R.id.tabAvailable);
        TextView tabChecked = view.findViewById(R.id.tabChecked);

        tabAll.setOnClickListener(v -> {
            selectTab(tabAll, tabAvailable, tabChecked);
            animateRecycler(recyclerView);
            adapter.filter("ALL");
        });

        tabAvailable.setOnClickListener(v -> {
            selectTab(tabAvailable, tabAll, tabChecked);
            animateRecycler(recyclerView);
            adapter.filter("AVAILABLE");
        });

        tabChecked.setOnClickListener(v -> {
            selectTab(tabChecked, tabAll, tabAvailable);
            animateRecycler(recyclerView);
            adapter.filter("CHECKED_OUT");
        });


        // Back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Notification
        ImageButton btnNotification = view.findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "New features incoming",
                        Toast.LENGTH_SHORT).show()
        );

        // FAB
        View fabScan = view.findViewById(R.id.fabScan);
        fabScan.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Scanner coming soon",
                        Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    private void selectTab(TextView selected, TextView... others) {

        selected.setSelected(true);
        selected.setTextColor(getResources().getColor(R.color.text_primary));

        for (TextView tv : others) {
            tv.setSelected(false);
            tv.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    private void animateRecycler(RecyclerView recyclerView) {

        recyclerView.setAlpha(0f);
        recyclerView.animate()
                .alpha(1f)
                .setDuration(250)
                .start();
    }


}
