package com.shop_here.service;

import com.shop_here.model.Product;
import com.shop_here.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public Product addProduct(Product product){
        return productRepo.save(product);
    }

    public Product getProduct(Long id){
        return productRepo.findById(id).orElse(null);
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
}
