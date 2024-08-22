package com.binnosoc.simulation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binnosoc.simulation.model.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
	 Optional<Role> findByName(String roleStudent);
}
