package hsf302.hsf302project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts")
@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CustomerID",nullable = false)
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private ProductEntity product;

    private Integer quantity;
}
