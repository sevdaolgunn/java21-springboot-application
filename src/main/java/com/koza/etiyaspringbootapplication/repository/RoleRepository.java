package com.koza.etiyaspringbootapplication.repository;

import com.koza.etiyaspringbootapplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long roleId);
    Optional<Role> findByRoleName(String roleName);


}
