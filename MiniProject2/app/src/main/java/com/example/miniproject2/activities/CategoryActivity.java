package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.CategoryAdapter;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Category;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private AppDatabase db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = AppDatabase.getInstance(this);
        rvCategories = findViewById(R.id.rvCategories);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        List<Category> categories = db.categoryDao().getAllCategories();
        adapter = new CategoryAdapter(categories, category -> {
            Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
            intent.putExtra("categoryId", category.getId());
            startActivity(intent);
        });
        rvCategories.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
