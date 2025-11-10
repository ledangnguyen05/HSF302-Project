package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.CartEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findByCustomer(UserEntity customer);
    CartEntity findByCustomerAndProduct(UserEntity customer, ProductEntity product);
}
