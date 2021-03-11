package com.example.shopping.repo;

import com.example.shopping.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface BankAccountRepo extends JpaRepository<BankAccount,Integer> {

    Optional<BankAccount> findByAppUserId(int appUserId);
}
