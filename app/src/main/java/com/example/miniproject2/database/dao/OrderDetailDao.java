package com.example.miniproject2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.miniproject2.database.entities.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId AND productId = :productId LIMIT 1")
    OrderDetail getOrderDetailByProduct(int orderId, int productId);
}
