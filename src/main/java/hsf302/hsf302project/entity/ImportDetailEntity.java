package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "IMPORT_DETAIL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportDetailEntity {

    @EmbeddedId
    private ImportDetailKey id;

    @ManyToOne
    @MapsId("receiptID")
    @JoinColumn(name = "ReceiptID", nullable = false)
    private ImportReceiptEntity receipt;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "ProductID", nullable = false)
    private ProductEntity product;


    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "UnitCost", nullable = false)
    private BigDecimal unitCost;
}
