package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailKey implements Serializable {
    @Column(name = "OrderID")
    private int orderId;

    @Column(name = "ProductID")
    private int productId;
}
