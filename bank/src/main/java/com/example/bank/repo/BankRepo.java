package com.example.bank.repo;

import com.example.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anonymity
 * @create 2019-08-15 13:55
 **/
@Repository
public interface BankRepo extends JpaRepository<Bank, Long> {
    Bank findByUsername(String username);
}
