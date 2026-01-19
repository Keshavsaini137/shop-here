package com.shop_here.controller;

import com.shop_here.dto.response.PageResponse;
import com.shop_here.dto.response.ProductResponse;
import com.shop_here.model.Product;
import com.shop_here.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public  Product getProductById(@PathVariable Long id){
        return  productService.getProduct(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Incoming request: {} {}", search, size);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

       PageResponse<ProductResponse> pageRes =  productService.getProducts(search, category, pageable);

//       PageResponse<Product> pageRes = new PageResponse<>(
//               prodPage.getContent(),
//               prodPage.getNumber(),
//               prodPage.getSize(),
//               prodPage.getTotalElements(),
//               prodPage.getTotalPages()
//       );

        return ResponseEntity.ok(pageRes);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/all")
    public List<Product> getAllProduct(){
        return productService.getAllProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted Successfully");
    }

}
