package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "IMPORT_RECEIPTS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceiptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReceiptID")
    private int receiptID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierID", nullable = false)
    private SupplierEntity supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private UserEntity employee;

    @NotNull
    @Column(name = "ImportDate", nullable = false, updatable = false)
    private LocalDateTime importDate = LocalDateTime.now();

    @DecimalMin(value = "0.00", message = "Total cost must be positive")
    @Column(name = "TotalCost", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "TotalQuantity", nullable = false)
    private int totalQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private ImportStatus status = ImportStatus.PENDING;

    public enum ImportStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }

    @Column(name = "Notes", length = 255)
    private String notes;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportDetailEntity> importDetails = new ArrayList<>();

    public void addImportDetail(ImportDetailEntity detail) {
        importDetails.add(detail);
        detail.setReceipt(this);

        if (detail.getSubTotal() != null)
            totalCost = totalCost.add(detail.getSubTotal());

        totalQuantity += detail.getQuantity();
    }
}
