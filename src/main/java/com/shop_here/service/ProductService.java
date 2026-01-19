package com.shop_here.service;

import com.shop_here.dto.response.PageResponse;
import com.shop_here.dto.response.ProductResponse;
import com.shop_here.model.Product;
import com.shop_here.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public PageResponse<ProductResponse> getProducts(
            String search,
            String category,
            Pageable pageable) {

        Page<Product> productPage = null;
        if (!search.isEmpty() && category != null) {
            productPage=  productRepo
                    .findByNameContainingIgnoreCaseAndCategory(search, category, pageable);
        }

        if (!search.isEmpty()) {
            productPage =  productRepo.findByNameContainingIgnoreCase(search, pageable);
        }

        if (category != null) {
            productPage =  productRepo.findByCategory(category, pageable);
        }

        productPage =  productRepo.findAll(pageable);

        List<ProductResponse> dtoList = productPage.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();

        return new PageResponse<ProductResponse>(
                dtoList,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    private ProductResponse mapToDTO(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }


    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product updateProduct(Long id, Product product){
        Product existing = productRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setImgUrl(product.getImgUrl());

        return productRepo.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
