package com.doziem.treading.repository;

import com.doziem.treading.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {

    Users findByEmail(String email);
}
