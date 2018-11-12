package com.sda.smartCalendar.domain.repository;

import com.sda.smartCalendar.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {

}
