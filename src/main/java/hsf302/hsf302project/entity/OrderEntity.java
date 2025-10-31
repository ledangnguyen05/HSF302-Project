package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private int orderID;
    @ManyToOne
    @JoinColumn(name = "CustomerID", nullable = false)
    private UserEntity customer;
    @ManyToOne
    @JoinColumn(name = "EmployeeID")
    private UserEntity employee;
    @Column(name = "OrderDate", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();
    @DecimalMin(value = "0.00", message = "Total amount must be positive")
    @Column(name = "TotalAmount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(name = "Status", nullable = false)
    private Status status = Status.PENDING;

    public enum Status {PENDING, CONFIRMED, IN_PREPARING, FINISHED, CANCELLED}

    @Column(name = "Notes", length = 255)
    private String notes;
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PaymentEntity> payments = new ArrayList<>();

    public void addOrderDetail(OrderDetailEntity detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
        totalAmount = totalAmount.add(detail.getSubTotal());
    }
}