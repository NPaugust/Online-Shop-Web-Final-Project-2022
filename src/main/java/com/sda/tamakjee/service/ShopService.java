package com.sda.tamakjee.service;

import com.sda.tamakjee.model.Product;
import com.sda.tamakjee.repository.CategoryRepository;
import com.sda.tamakjee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void addProduct(Product product){
        productRepository.save(product);
    }


}
