package com.example.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.entities.OrderDetail;
import com.example.miniproject2.database.entities.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<OrderDetail> orderDetails;
    private AppDatabase db;
    private OnItemRemovedListener listener;

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    public CartAdapter(List<OrderDetail> orderDetails, AppDatabase db, OnItemRemovedListener listener) {
        this.orderDetails = orderDetails;
        this.db = db;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        Product product = db.productDao().getProductById(detail.getProductId());
        
        if (product != null) {
            holder.tvName.setText(product.getName());
            holder.tvPrice.setText("$" + String.format("%.2f", detail.getUnitPrice()));
            holder.tvQuantity.setText("x" + detail.getQuantity());
            holder.tvSubtotal.setText("$" + String.format("%.2f", detail.getQuantity() * detail.getUnitPrice()));
        }

        holder.btnRemove.setOnClickListener(v -> {
            db.orderDetailDao().delete(detail);
            orderDetails.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orderDetails.size());
            if (listener != null) {
                listener.onItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvSubtotal;
        ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartProductName);
            tvPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvCartSubtotal);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}
