package hsf302.hsf302project.service.impl;

import hsf302.hsf302project.entity.CartEntity;
import hsf302.hsf302project.repository.CartRepository;
import hsf302.hsf302project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<CartEntity> findAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public boolean addCart(CartEntity cart) {
        cartRepository.save(cart);
        return true;
    }

    @Override
    public boolean updateCart(Long id, CartEntity cart) {
        if (cartRepository.existsById(id)) {
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCart(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public CartEntity findByCartId(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updateQuantity(Long id, int quantity) {
        CartEntity cart = cartRepository.findById(id).orElse(null);
        if (cart != null) {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

}
