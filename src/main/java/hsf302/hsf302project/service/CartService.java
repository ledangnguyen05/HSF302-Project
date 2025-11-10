package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.CartEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.entity.UserEntity;

import java.util.List;

public interface CartService {
    List<CartEntity> findAllCarts();
    void addProductToCart(int userId, int productId);
    boolean updateCart(Long id, CartEntity cart);
    boolean deleteCart(Long id);
    CartEntity findByCartId(Long id);
    boolean updateQuantity(Long id, int quantity);
}
