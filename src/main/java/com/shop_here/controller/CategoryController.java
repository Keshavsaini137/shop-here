package com.shop_here.controller;

import com.shop_here.model.Category;
import com.shop_here.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category){
        return  categoryService.addCategory(category);
    }

    @GetMapping("/getAll")
    public List<Category> getAllCategory(){
        return  categoryService.getAllCategories();
    }
}
