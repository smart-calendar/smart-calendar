package com.sda.smartCalendar.service;

import com.sda.smartCalendar.controller.modelDTO.EventDTO;
import com.sda.smartCalendar.controller.modelDTO.UserRegistrationDTO;
import com.sda.smartCalendar.domain.model.Event;
import com.sda.smartCalendar.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class MappingService {

    public User map(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setProvider(userRegistrationDTO.getProvider());
        return user;
    }

    public Event map (EventDTO eventDTO){
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setEvent_start(eventDTO.getEvent_start());
        event.setEvent_finish(eventDTO.getEvent_finish());
        event.setCategory(eventDTO.getCategory());
        event.setUser(eventDTO.getUser());
        return event;
    }

    public EventDTO map (Event event){
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName(event.getName());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEvent_start(event.getEvent_start());
        eventDTO.setEvent_finish(event.getEvent_finish());
        eventDTO.setCategory(event.getCategory());
        eventDTO.setUser(event.getUser());
        return eventDTO;
    }


}