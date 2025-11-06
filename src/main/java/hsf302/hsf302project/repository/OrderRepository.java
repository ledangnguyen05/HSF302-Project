package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.OrderEntity;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity>findOrdersByCustomer_fullNameContainingIgnoreCase(String customer_fullName);
    List<OrderEntity>findOrdersByEmployee_fullNameContainingIgnoreCase(String employee_fullName);
    List<OrderEntity>findOrdersByOrderDateBetween(LocalDateTime orderDate, LocalDateTime orderDate2);
    List<OrderEntity> findOrdersByStatus(OrderEntity.Status status);

    List<OrderEntity> findByOrderDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, OrderEntity.Status status);
    long countByStatus(OrderEntity.Status status);
}
