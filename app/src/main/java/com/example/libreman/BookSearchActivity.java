package com.example.libreman;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        RecyclerView recyclerView = findViewById(R.id.rvBooks);

        // IMPORTANT: LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter
        BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
    }
}
