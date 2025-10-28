package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<CategoryEntity> findByCategoryNameContaining(String categoryName) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }

    @Override
    public boolean addCategory(CategoryEntity category) {
        try{
            categoryRepository.save(category);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean updateCategory(int categoryId, CategoryEntity category) {
        try{
            if(categoryRepository.existsById(categoryId)) {
                category.setId(categoryId);
                categoryRepository.save(category);
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        try{
            categoryRepository.deleteById(categoryId);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity findByCategoryId(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }
}
