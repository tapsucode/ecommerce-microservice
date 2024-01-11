package com.phamanh.accountservice.repositories;


import com.phamanh.accountservice.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsername(String username);

}
