package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryEntity> findByCategoryNameContaining(String categoryName);
    boolean addCategory(CategoryEntity category);
    boolean updateCategory(int categoryId, CategoryEntity category);
    boolean deleteCategory(int categoryId);
    List<CategoryEntity> findAllCategories();
    CategoryEntity findByCategoryId(int categoryId);
}
