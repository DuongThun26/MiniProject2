package com.example.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.miniproject2.R;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.Order;
import com.example.miniproject2.database.entities.OrderDetail;
import com.example.miniproject2.database.entities.Product;
import com.example.miniproject2.utils.SessionManager;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    private TextView tvOrderId, tvDate, tvUser, tvDetails, tvTotal;
    private Button btnHome;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);
        orderId = getIntent().getIntExtra("orderId", -1);

        tvOrderId = findViewById(R.id.tvInvoiceOrderId);
        tvDate = findViewById(R.id.tvInvoiceDate);
        tvUser = findViewById(R.id.tvInvoiceUser);
        tvDetails = findViewById(R.id.tvInvoiceDetails);
        tvTotal = findViewById(R.id.tvInvoiceTotal);
        btnHome = findViewById(R.id.btnBackToHome);

        loadInvoice();

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void loadInvoice() {
        Order order = db.orderDao().getOrderById(orderId);
        if (order != null) {
            tvOrderId.setText("Order ID: #" + order.getId());
            tvDate.setText("Date: " + order.getOrderDate());
            tvUser.setText("Customer: " + sessionManager.getUsername());
            tvTotal.setText("Total Paid: $" + String.format("%.2f", order.getTotalAmount()));

            List<OrderDetail> details = db.orderDetailDao().getOrderDetailsByOrder(orderId);
            StringBuilder sb = new StringBuilder();
            for (OrderDetail detail : details) {
                Product p = db.productDao().getProductById(detail.getProductId());
                String productName = (p != null) ? p.getName() : "Unknown Product";
                sb.append(productName)
                        .append(" x")
                        .append(detail.getQuantity())
                        .append(" - $")
                        .append(String.format("%.2f", detail.getQuantity() * detail.getUnitPrice()))
                        .append("\n");
            }
            tvDetails.setText(sb.toString());
        }
    }
}
