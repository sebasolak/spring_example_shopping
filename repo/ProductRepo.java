package com.example.shopping.repo;

import com.example.shopping.model.AppUser;
import com.example.shopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ProductRepo extends JpaRepository<Product,Integer> {
//    @Query("SELECT COUNT(*) from product WHERE id = ?1 and username = ?2")
    Product findProductByIdAndAppUserUsername(int id, String appUserUsername);

    List<Product> findProductsByAppUserUsername(String appUserUsername);
}
