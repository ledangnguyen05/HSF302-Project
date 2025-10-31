package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.OrderEntity;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity>findOrdersByCustomer_fullNameContaining(String customer_fullName);
    List<OrderEntity>findOrdersByEmployee_fullNameContaining(String employee_fullName);
    List<OrderEntity>findOrdersByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<OrderEntity> findOrdersByPayments_status(String paymentsStatus);

}
