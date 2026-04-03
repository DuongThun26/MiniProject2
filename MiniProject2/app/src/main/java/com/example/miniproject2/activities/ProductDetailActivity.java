package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.miniproject2.R;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Order;
import com.example.miniproject2.database.entities.OrderDetail;
import com.example.miniproject2.database.entities.Product;
import com.example.miniproject2.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvName, tvPrice, tvDesc;
    private Button btnAddToCart;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int productId;
    private Product product;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        productId = getIntent().getIntExtra("productId", -1);
        product = db.productDao().getProductById(productId);

        ivProductImage = findViewById(R.id.ivProductImage);
        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDesc = findViewById(R.id.tvDetailDesc);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (product != null) {
            tvName.setText(product.getName());
            tvPrice.setText("$" + String.format("%.2f", product.getPrice()));
            tvDesc.setText(product.getDescription());
        }

        btnAddToCart.setOnClickListener(v -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Please login to add to cart", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            addToCart();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToCart() {
        int userId = sessionManager.getUserId();
        Order pendingOrder = db.orderDao().getPendingOrderByUser(userId);

        long orderId;
        if (pendingOrder == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Order newOrder = new Order(userId, currentDate, 0, "Pending");
            orderId = db.orderDao().insert(newOrder);
        } else {
            orderId = pendingOrder.getId();
        }

        OrderDetail existingDetail = db.orderDetailDao().getOrderDetailByProduct((int) orderId, productId);
        if (existingDetail != null) {
            existingDetail.setQuantity(existingDetail.getQuantity() + 1);
            db.orderDetailDao().update(existingDetail);
        } else {
            OrderDetail detail = new OrderDetail((int) orderId, productId, 1, product.getPrice());
            db.orderDetailDao().insert(detail);
        }

        showAfterAddDialog();
    }

    private void showAfterAddDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Product added to cart. What would you like to do next?")
                .setPositiveButton("Checkout", (dialog, which) -> {
                    startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                    finish();
                })
                .setNegativeButton("Continue Shopping", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}
