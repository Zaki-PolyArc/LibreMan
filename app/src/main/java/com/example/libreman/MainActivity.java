package com.example.libreman;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_catalog);

        RecyclerView recyclerView = findViewById(R.id.recyclerBooks);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        int spacingInPx = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(
                new GridSpacingItemDecoration(2, spacingInPx, false)
        );

        recyclerView.setAdapter(new CatalogBookAdapter());

    }
}
