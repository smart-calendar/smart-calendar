package com.sda.smartCalendar.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private Long id;

    @Column
    private String name;

    public Product(String name){
        this.name=name;
    }

    @ManyToOne
    @JoinColumn(name="user_email")
    private User user;


}
