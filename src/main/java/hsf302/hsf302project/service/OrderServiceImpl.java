package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.OrderDetailEntity;
import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity findById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public boolean create(OrderEntity order) {
        try {
            order.setOrderDate(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            order.setTotalAmount(BigDecimal.ZERO);

            // Ensure all details are linked & subtotal calculated
            if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                for (OrderDetailEntity detail : order.getOrderDetails()) {
                    detail.setOrder(order);
                    detail.calculateSubTotal();
                    order.setTotalAmount(order.getTotalAmount().add(detail.getSubTotal()));
                }
            }

            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(int orderId, OrderEntity newOrderData) {
        try {
            OrderEntity existingOrder = orderRepository.findById(orderId).orElse(null);
            if (existingOrder == null) return false;

            existingOrder.setStatus(newOrderData.getStatus());
            existingOrder.setNotes(newOrderData.getNotes());
            existingOrder.setEmployee(newOrderData.getEmployee());
            existingOrder.setUpdatedAt(LocalDateTime.now());

            // Clear existing details if changed
            existingOrder.getOrderDetails().clear();
            existingOrder.setTotalAmount(BigDecimal.ZERO);

            if (newOrderData.getOrderDetails() != null && !newOrderData.getOrderDetails().isEmpty()) {
                for (OrderDetailEntity detail : newOrderData.getOrderDetails()) {
                    detail.setOrder(existingOrder);
                    detail.calculateSubTotal();
                    existingOrder.getOrderDetails().add(detail);
                    existingOrder.setTotalAmount(existingOrder.getTotalAmount().add(detail.getSubTotal()));
                }
            }

            orderRepository.save(existingOrder);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        try {
            if (orderRepository.existsById(id)) {
                orderRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
