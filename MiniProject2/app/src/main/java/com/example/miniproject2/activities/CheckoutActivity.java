package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.miniproject2.R;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Order;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvTotal, tvDate;
    private Button btnPayNow;
    private AppDatabase db;
    private int orderId;
    private Order order;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = AppDatabase.getInstance(this);
        orderId = getIntent().getIntExtra("orderId", -1);
        order = db.orderDao().getOrderById(orderId);

        tvTotal = findViewById(R.id.tvCheckoutTotal);
        tvDate = findViewById(R.id.tvCheckoutDate);
        btnPayNow = findViewById(R.id.btnPayNow);
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        if (order != null) {
            tvTotal.setText("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
            tvDate.setText("Order Date: " + order.getOrderDate());
        }

        btnPayNow.setOnClickListener(v -> {
            if (order != null) {
                order.setStatus("Paid");
                db.orderDao().update(order);
                Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, InvoiceActivity.class);
                intent.putExtra("orderId", order.getId());
                startActivity(intent);
                finish();
            }
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
}
