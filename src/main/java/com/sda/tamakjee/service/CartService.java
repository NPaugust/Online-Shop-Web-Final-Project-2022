package com.sda.tamakjee.service;

import com.sda.tamakjee.model.Cart;
import com.sda.tamakjee.model.CartItem;
import com.sda.tamakjee.model.Product;
import com.sda.tamakjee.model.User;
import com.sda.tamakjee.repository.CartItemRepository;
import com.sda.tamakjee.repository.CartRepository;
import com.sda.tamakjee.repository.ProductRepository;
import com.sda.tamakjee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public void addCartItem(Product product, Integer requestedQuantity, User user) {
        Cart cart = getOpenCart(user);
        Optional<CartItem> optionalCartItem = cartItemRepository.getCartItemByProductAndCart(product, cart);
        CartItem cartItem = optionalCartItem.orElse(new CartItem(product, 0, cart));
        if (product.getQuantity() >= cartItem.getQuantity() + requestedQuantity) {
            cartItem.setQuantity(cartItem.getQuantity() + requestedQuantity);
        }
        if (cartItem.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItemRepository.save(cartItem);
        }

    }

    public Cart getOpenCart(User user) {
        Optional<Cart> optionalCart = cartRepository.getCartByStatusAndUser("open", user);
        Cart cart = optionalCart.orElse(new Cart(user, "open"));
        return cartRepository.save(cart);
    }

    public Integer getNumberOfCartItems(User user) {//fiseaza in navbar cantitatea produselor
        Cart cart = getOpenCart(user);
        int itemSum = 0;
//        rtItem cartItem: cart)
        for (int i = 0; i < cartItemRepository.getCartItemsByCart(cart).size(); i++) {
            itemSum += cartItemRepository.getCartItemsByCart(cart).get(i).getQuantity();

        }
        return itemSum;
//        return cartItemRepository.getCartItemsByCart(cart).size();//afiseaza doar produsele in cart in navbar

    }

    public boolean checkout(User user) {
        Cart cart = getOpenCart(user);

        if (cart.getCartItemSet().size() > 0) {
            //cart is valid
            boolean allElementsValid = true;
            for (CartItem cartItem : cart.getCartItemSet()) {
                if (cartItem.getQuantity() > productRepository.getOne(cartItem.getProduct().getId()).getQuantity())
                    allElementsValid = false;
            }
            if (allElementsValid) {
                for (CartItem cartItem : cart.getCartItemSet()) {
                    Product product = cartItem.getProduct();
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);
                }
                cart.setStatus("closed");
                cartRepository.save(cart);
            }
            return allElementsValid;

        }
        return false;
    }

}
