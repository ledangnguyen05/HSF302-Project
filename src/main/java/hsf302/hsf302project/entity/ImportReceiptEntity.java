package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table(name = "IMPORT_RECEIPT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReceiptID")
    private int receiptID;

    @ManyToOne
    @JoinColumn(name = "SupplierID", nullable = false)
    private SupplierEntity supplier;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private UserEntity employee;

    @Column(name = "ImportDate", nullable = false)
    private LocalDate importDate;

    @Column(name = "TotalCost", nullable = false)
    private BigDecimal totalCost;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<ImportDetailEntity> importDetails = new ArrayList<>();
}
