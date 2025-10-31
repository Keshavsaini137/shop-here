package com.shop_here.controller;

import com.shop_here.model.Product;
import com.shop_here.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("add")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/{id}")
    public  Product getProduct(@PathVariable Long id){
        return  productService.getProduct(id);
    }

    @GetMapping("/all")
    public List<Product> getAllProduct(){
        return productService.getAllProducts();
    }
}
