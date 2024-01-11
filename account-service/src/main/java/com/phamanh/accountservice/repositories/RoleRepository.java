package com.phamanh.accountservice.repositories;



import com.phamanh.accountservice.domains.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository
    extends JpaRepository<Role, Long> {
        Optional<Role> findByName(Role.Name name);
}
