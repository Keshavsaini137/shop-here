package com.shop_here.service;

import com.shop_here.model.Category;
import com.shop_here.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    public Category addCategory(Category category){
        return categoryRepo.save(category);
    }

    public List<Category> getAllCategories(){
        return  categoryRepo.findAll();
    }
}
