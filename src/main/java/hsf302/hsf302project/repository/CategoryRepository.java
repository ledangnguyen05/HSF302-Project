package hsf302.hsf302project.repository;

import hsf302.hsf302project.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity>findByCategoryNameContainingIgnoreCase(String categoryName);
    Optional<CategoryEntity> findByCategoryNameIgnoreCase(String categoryName);
}
