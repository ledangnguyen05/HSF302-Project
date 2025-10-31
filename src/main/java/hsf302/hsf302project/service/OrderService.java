package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    List<OrderEntity> findAll();
    OrderEntity findById(int orderId);
    boolean create(OrderEntity order);
    boolean update(int orderId, OrderEntity order);
    boolean delete(int id);
}
