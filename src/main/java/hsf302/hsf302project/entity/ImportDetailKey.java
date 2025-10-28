package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailKey implements Serializable {

    @Column(name = "ReceiptID")
    private int receiptID;

    @Column(name = "ProductID")
    private int productId;
}
