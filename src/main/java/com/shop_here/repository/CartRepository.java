package com.shop_here.repository;

import com.shop_here.model.Cart;
import com.shop_here.model.CartItem;
import com.shop_here.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(Long user);


}
