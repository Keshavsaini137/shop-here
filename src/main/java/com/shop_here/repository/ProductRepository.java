package com.shop_here.repository;

import com.shop_here.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByPriceBetween(double min, double max, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndCategory(
            String name, String category, Pageable pageable);
}
