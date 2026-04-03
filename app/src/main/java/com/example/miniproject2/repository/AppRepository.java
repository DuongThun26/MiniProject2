package com.example.miniproject2.repository;

import android.content.Context;
import com.example.miniproject2.database.AppDatabase;
import com.example.miniproject2.database.dao.*;
import com.example.miniproject2.database.entities.*;
import java.util.List;

public class AppRepository {
    private UserDao userDao;
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private OrderDao orderDao;
    private OrderDetailDao orderDetailDao;

    public AppRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        productDao = db.productDao();
        categoryDao = db.categoryDao();
        orderDao = db.orderDao();
        orderDetailDao = db.orderDetailDao();
    }

    // User operations
    public void insertUser(User user) { userDao.insert(user); }
    public User login(String username, String password) { return userDao.login(username, password); }
    public User getUserByUsername(String username) { return userDao.getUserByUsername(username); }

    // Category operations
    public List<Category> getAllCategories() { return categoryDao.getAllCategories(); }
    public void insertCategory(Category category) { categoryDao.insert(category); }

    // Product operations
    public List<Product> getAllProducts() { return productDao.getAllProducts(); }
    public List<Product> getProductsByCategory(int categoryId) { return productDao.getProductsByCategory(categoryId); }
    public Product getProductById(int productId) { return productDao.getProductById(productId); }
    public void insertProduct(Product product) { productDao.insert(product); }

    // Order operations
    public long insertOrder(Order order) { return orderDao.insert(order); }
    public void updateOrder(Order order) { orderDao.update(order); }
    public Order getPendingOrderByUser(int userId) { return orderDao.getPendingOrderByUser(userId); }
    public Order getOrderById(int orderId) { return orderDao.getOrderById(orderId); }

    // OrderDetail operations
    public void insertOrderDetail(OrderDetail detail) { orderDetailDao.insert(detail); }
    public void updateOrderDetail(OrderDetail detail) { orderDetailDao.update(detail); }
    public List<OrderDetail> getOrderDetailsByOrder(int orderId) { return orderDetailDao.getOrderDetailsByOrder(orderId); }
    public OrderDetail getOrderDetailByProduct(int orderId, int productId) { return orderDetailDao.getOrderDetailByProduct(orderId, productId); }
}
