package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductEntity> findByCategory(CategoryEntity category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<ProductEntity> findByProdName(String prodName) {
        return productRepository.findByProductNameContainingIgnoreCase(prodName);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public boolean addProd(ProductEntity product) {
        return productRepository.save(product) != null;
    }

    @Override
    public void deleteProd(int prodId) {
        productRepository.deleteById(prodId);
    }

    @Override
    public boolean updateProd(int prodId, ProductEntity product) {
        ProductEntity existedProduct = productRepository.findById(prodId).orElse(null);
        if (existedProduct != null) {
            existedProduct.setProductName(product.getProductName());
            existedProduct.setUnitPrice(product.getUnitPrice());
            existedProduct.setStockQuantity(product.getStockQuantity());
            existedProduct.setDescription(product.getDescription());
            existedProduct.setCategory(product.getCategory());
            existedProduct.setSupplier(product.getSupplier());
            existedProduct.setImagePath(product.getImagePath());
            productRepository.save(existedProduct);
            return true;
        }
        return false;
    }

    @Override
    public ProductEntity findById(int id) {
        return productRepository.findById(id).orElse(null);
    }
}
