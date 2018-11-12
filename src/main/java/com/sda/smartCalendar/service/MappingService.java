package com.sda.smartCalendar.service;

import com.sda.smartCalendar.controller.modelDTO.EventDTO;
import com.sda.smartCalendar.controller.modelDTO.ProductDTO;
import com.sda.smartCalendar.controller.modelDTO.UserDTO;
import com.sda.smartCalendar.controller.modelDTO.UserRegistrationDTO;
import com.sda.smartCalendar.domain.model.Event;
import com.sda.smartCalendar.domain.model.Product;
import com.sda.smartCalendar.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MappingService {

    public User map(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setProvider(userRegistrationDTO.getProvider());
        user.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        return user;
    }

    public Event map (EventDTO eventDTO){
        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setEvent_finish(eventDTO.getEvent_finish());
        event.setEvent_start(eventDTO.getEvent_start());
        event.setCategory(eventDTO.getCategory());
        event.setUser(eventDTO.getUser());
        return event;
    }

    public EventDTO map (Event event){
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEvent_finish(event.getEvent_finish());
        eventDTO.setEvent_start(event.getEvent_start());
        eventDTO.setCategory(event.getCategory());
        eventDTO.setUser(event.getUser());
        return eventDTO;
    }

    public UserRegistrationDTO map(User user) {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail(user.getEmail());
        userRegistrationDTO.setFirstName(user.getFirstName());
        userRegistrationDTO.setLastName(user.getLastName());
        userRegistrationDTO.setPassword(user.getPassword());
        userRegistrationDTO.setProvider(user.getProvider());
        userRegistrationDTO.setPhoneNumber(user.getPhoneNumber());
        return userRegistrationDTO;
    }

    public ProductDTO map (Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        return productDTO;
    }

    public Product map (ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        return product;
    }

}
