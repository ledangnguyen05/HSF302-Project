package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findByProductNameContainingIgnoreCase(String productName);

    List<ProductEntity> findByCategory(CategoryEntity category);
}
