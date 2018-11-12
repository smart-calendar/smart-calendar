package com.sda.smartCalendar.service;

import com.sda.smartCalendar.controller.modelDTO.ProductDTO;
import com.sda.smartCalendar.domain.model.Product;
import com.sda.smartCalendar.domain.model.User;
import com.sda.smartCalendar.domain.repository.ProductRepository;
import com.sda.smartCalendar.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingService mappingService;

    public void addProduct (ProductDTO productDTO, Principal principal){
        User user = userRepository.findByEmail(principal.getName());
        Product product = null;
        product = mappingService.map(productDTO);
        product.setUser(user);
        productRepository.save(product);
    }



}
