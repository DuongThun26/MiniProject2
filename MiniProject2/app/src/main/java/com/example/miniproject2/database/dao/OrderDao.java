package com.example.miniproject2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.miniproject2.database.entities.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'Pending' LIMIT 1")
    Order getPendingOrderByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrdersByUser(int userId);
}
