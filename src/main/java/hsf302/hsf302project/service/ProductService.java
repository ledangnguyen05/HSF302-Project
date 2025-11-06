package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> findByCategory(CategoryEntity category);

    List<ProductEntity> findByCategoryId(int categoryId);

    List<ProductEntity> findByProdName(String prodName);

    List<ProductEntity> getAllProducts();

    boolean addProd(ProductEntity product);

    void deleteProd(int prodId);

    boolean updateProd(int prodId, ProductEntity product);

    ProductEntity findById(int id);
}
