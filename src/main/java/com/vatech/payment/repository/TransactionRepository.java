package com.vatech.payment.repository;

import com.vatech.payment.entity.User;
import com.vatech.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    List<Transaction> findByAccountUser(User user);

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.accountId = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);
}