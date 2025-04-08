package com.vatech.payment.repository;

import com.vatech.payment.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Custom query to find an account by username
//    Account findByUserId(Long userId);
    @Query("SELECT a FROM Account a JOIN a.user u WHERE u.username = :username")
    Account findAccountByUsername(@Param("username") String username);
}