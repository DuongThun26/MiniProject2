package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject2.R;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Category;
import com.example.miniproject2.database.entities.Product;
import com.example.miniproject2.database.entities.User;
import com.example.miniproject2.utils.SessionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AppDatabase db;
    private Button btnLogin, btnViewProducts, btnViewCategories;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        initViews();
        initData();
        setupListeners();
    }

    private void initViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnViewCategories = findViewById(R.id.btnViewCategories);
        tvWelcome = findViewById(R.id.tvWelcome);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                sessionManager.logout();
                updateUI();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnViewProducts.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProductListActivity.class));
        });

        btnViewCategories.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (sessionManager.isLoggedIn()) {
            btnLogin.setText("Logout");
            tvWelcome.setVisibility(View.VISIBLE);
            tvWelcome.setText("Welcome, " + sessionManager.getUsername() + "!");
        } else {
            btnLogin.setText("Login");
            tvWelcome.setVisibility(View.GONE);
        }
    }

    private void initData() {
        // Add dummy data if tables are empty
        if (db.userDao().getUserByUsername("admin") == null) {
            db.userDao().insert(new User("admin", "admin", "Administrator"));
            db.userDao().insert(new User("user", "user", "Standard User"));
        }

        List<Category> categories = db.categoryDao().getAllCategories();
        if (categories == null || categories.isEmpty()) {
            // Inserting Categories
            db.categoryDao().insert(new Category("Fruits", "Fresh seasonal fruits"));
            db.categoryDao().insert(new Category("Vegetables", "Organic vegetables"));
            db.categoryDao().insert(new Category("Tropical", "Exotic tropical fruits"));
            db.categoryDao().insert(new Category("Berries", "Sweet and sour berries"));
            
            categories = db.categoryDao().getAllCategories();
            
            // Getting IDs based on names (to ensure correct mapping)
            int fruitId = 1;
            int veggieId = 2;
            int tropicalId = 3;
            int berryId = 4;
            
            for(Category c : categories) {
                if(c.getName().equals("Fruits")) fruitId = c.getId();
                if(c.getName().equals("Vegetables")) veggieId = c.getId();
                if(c.getName().equals("Tropical")) tropicalId = c.getId();
                if(c.getName().equals("Berries")) berryId = c.getId();
            }
            
            // Fruits
            db.productDao().insert(new Product("Apple", 2.5, "Sweet red apple", "apple_url", fruitId, "2025-12-31"));
            db.productDao().insert(new Product("Banana", 1.2, "Ripe yellow banana", "banana_url", fruitId, "2025-12-31"));
            db.productDao().insert(new Product("Orange", 3.0, "Juicy orange", "orange_url", fruitId, "2025-12-31"));
            db.productDao().insert(new Product("Grapes", 4.5, "Fresh green grapes", "grapes_url", fruitId, "2025-12-31"));
            
            // Vegetables
            db.productDao().insert(new Product("Carrot", 0.8, "Crunchy orange carrot", "carrot_url", veggieId, "2025-12-31"));
            db.productDao().insert(new Product("Broccoli", 1.5, "Fresh green broccoli", "broccoli_url", veggieId, "2025-12-31"));
            db.productDao().insert(new Product("Tomato", 1.0, "Red ripe tomato", "tomato_url", veggieId, "2025-12-31"));
            
            // Tropical
            db.productDao().insert(new Product("Mango", 3.5, "Sweet tropical mango", "mango_url", tropicalId, "2025-12-31"));
            db.productDao().insert(new Product("Pineapple", 5.0, "Fresh pineapple", "pineapple_url", tropicalId, "2025-12-31"));
            db.productDao().insert(new Product("Durian", 15.0, "King of fruits", "durian_url", tropicalId, "2025-12-31"));
            
            // Berries
            db.productDao().insert(new Product("Strawberry", 6.0, "Sweet red strawberries", "strawberry_url", berryId, "2025-12-31"));
            db.productDao().insert(new Product("Blueberry", 8.5, "Fresh blueberries", "blueberry_url", berryId, "2025-12-31"));
            db.productDao().insert(new Product("Raspberry", 9.0, "Tart red raspberries", "raspberry_url", berryId, "2025-12-31"));
        }
    }
}
