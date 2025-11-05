package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.OrderDetailEntity;
import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.repository.OrderRepository;
import hsf302.hsf302project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

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
            for (OrderDetailEntity detail : order.getOrderDetails()) {
                var product = productRepository.findById(detail.getProduct().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

                detail.setProduct(product);
                detail.setUnitPrice(product.getUnitPrice());
                detail.setOrder(order);
                detail.calculateSubTotal();
            }

            BigDecimal total = order.getOrderDetails().stream()
                    .map(OrderDetailEntity::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);

            // ✅ In ra thông tin đơn hàng sau khi lưu
            System.out.println("=== ORDER SAVED SUCCESSFULLY ===");
            System.out.println("Order ID: " + order.getOrderID());
            System.out.println("Customer: " + order.getCustomer().getFullName());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("Notes: " + order.getNotes());
            System.out.println("Order Details:");

            for (OrderDetailEntity detail : order.getOrderDetails()) {
                System.out.println("   Product ID: " + detail.getProduct().getId());
                System.out.println("   Product Name: " + detail.getProduct().getProductName());
                System.out.println("   Unit Price: " + detail.getProduct().getUnitPrice());
                System.out.println("   Quantity: " + detail.getQuantity());
                System.out.println("   Subtotal: " + detail.getSubTotal());
                System.out.println("---------------------------");
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

    @Override
    public double getRevenueByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<OrderEntity> orders = orderRepository.findByOrderDateBetweenAndStatus(start, end, OrderEntity.Status.FINISHED);

        if (orders == null || orders.isEmpty())
            return 0.0;

        double totalRevenue = 0.0;
        for (OrderEntity order : orders)
            totalRevenue += order.getTotalAmount().doubleValue();

        return totalRevenue;
    }

    @Override
    public double getRevenueByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<OrderEntity> orders = orderRepository.findByOrderDateBetweenAndStatus(start, end, OrderEntity.Status.FINISHED);

        if (orders == null || orders.isEmpty())
            return 0.0;

        double totalRevenue = 0.0;
        for (OrderEntity order : orders)
            totalRevenue += order.getTotalAmount().doubleValue();

        return totalRevenue;
    }

    @Override
    public long countOrdersByStatus(OrderEntity.Status status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public List<OrderEntity> getFinishedOrdersByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<OrderEntity> orders = orderRepository.findByOrderDateBetweenAndStatus(start, end, OrderEntity.Status.FINISHED);

        if (orders == null)
            return new ArrayList<>();

        return orders;
    }
}
