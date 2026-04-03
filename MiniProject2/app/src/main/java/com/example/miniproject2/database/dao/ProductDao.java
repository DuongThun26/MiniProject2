package com.example.miniproject2.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.miniproject2.database.entities.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getProductById(int productId);
}
