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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.adapters.CartAdapter;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Order;
import com.example.miniproject2.database.entities.OrderDetail;
import com.example.miniproject2.utils.SessionManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private TextView tvTotal;
    private Button btnContinue, btnCheckout;
    private AppDatabase db;
    private SessionManager sessionManager;
    private CartAdapter adapter;
    private Order currentOrder;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvCartTotal);
        btnContinue = findViewById(R.id.btnContinueShopping);
        btnCheckout = findViewById(R.id.btnCheckout);
        
        // Setup Toolbar for Back Button
        toolbar = findViewById(R.id.toolbar);
        
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

        rvCart.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnContinue.setOnClickListener(v -> {
            finish(); // Simply go back to previous screen (Products)
        });

        btnCheckout.setOnClickListener(v -> {
            if (currentOrder != null && currentOrder.getTotalAmount() > 0) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                intent.putExtra("orderId", currentOrder.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
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

    private void loadCart() {
        int userId = sessionManager.getUserId();
        currentOrder = db.orderDao().getPendingOrderByUser(userId);

        if (currentOrder != null) {
            List<OrderDetail> details = db.orderDetailDao().getOrderDetailsByOrder(currentOrder.getId());
            adapter = new CartAdapter(details, db);
            rvCart.setAdapter(adapter);

            double total = 0;
            for (OrderDetail detail : details) {
                total += detail.getQuantity() * detail.getUnitPrice();
            }
            tvTotal.setText("$" + String.format("%.2f", total));
            
            currentOrder.setTotalAmount(total);
            db.orderDao().update(currentOrder);
        } else {
            tvTotal.setText("$0.00");
        }
    }
}
