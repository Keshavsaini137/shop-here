package com.shop_here.repository;

import com.shop_here.model.Cart;
import com.shop_here.model.CartItem;
import com.shop_here.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

