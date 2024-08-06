package com.koza.etiyaspringbootapplication.repository;

import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
Optional <User> findById(Long userId);

}
