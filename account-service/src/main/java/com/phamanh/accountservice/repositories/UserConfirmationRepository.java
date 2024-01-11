package com.phamanh.accountservice.repositories;


import com.phamanh.accountservice.domains.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConfirmationRepository extends JpaRepository<ConfirmationCode, Long> {

    List<ConfirmationCode> findByAccountIdAndStatus(Long accountId, ConfirmationCode.Status status);

    Optional<ConfirmationCode> findByAccountIdAndCodeAndStatus(Long accountId, String code, ConfirmationCode.Status status);

}
