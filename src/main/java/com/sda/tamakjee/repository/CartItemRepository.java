package com.sda.tamakjee.repository;

import com.sda.tamakjee.model.Cart;
import com.sda.tamakjee.model.CartItem;
import com.sda.tamakjee.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> getCartItemByProductAndCart(Product product, Cart cart);

    List<CartItem> getCartItemsByCart(Cart cart);

}

