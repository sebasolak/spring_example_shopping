package com.example.shopping.repo;

import com.example.shopping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CartRepo extends JpaRepository<Cart, Integer> {

}
