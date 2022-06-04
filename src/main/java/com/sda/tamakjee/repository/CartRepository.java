package com.sda.tamakjee.repository;

import com.sda.tamakjee.model.Cart;
import com.sda.tamakjee.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
//    Cart fiindCartByUser(User user);
//
//    List<Cart> findCartsForUser(User user);
    Optional<Cart> getCartByStatusAndUser(String status, User user);
    Optional<Cart> getCartByStatusAndUser_Id(String status,Long userId);
}

