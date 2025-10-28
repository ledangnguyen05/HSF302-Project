package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    private OrderEntity order;

    @Column(name = "PaymentMethod", nullable = false)
    private String paymentMethod;

    @Column(name = "PaymentDate")
    private LocalDate paymentDate;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "Status", nullable = false)
    private String status;
}
