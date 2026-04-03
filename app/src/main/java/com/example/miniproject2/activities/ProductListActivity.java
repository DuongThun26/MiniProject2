package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.ProductAdapter;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Product;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private AppDatabase db;
    private TextView tvTitle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        db = AppDatabase.getInstance(this);
        rvProducts = findViewById(R.id.rvProducts);
        tvTitle = findViewById(R.id.tvTitle);
        toolbar = findViewById(R.id.toolbar);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        int categoryId = getIntent().getIntExtra("categoryId", -1);
        List<Product> products;
        if (categoryId != -1) {
            products = db.productDao().getProductsByCategory(categoryId);
            tvTitle.setText("Category Products");
        } else {
            products = db.productDao().getAllProducts();
            tvTitle.setText("All Products");
        }

        adapter = new ProductAdapter(products, product -> {
            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            startActivity(intent);
        });
        rvProducts.setAdapter(adapter);
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
