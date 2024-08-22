package com.binnosoc.simulation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binnosoc.simulation.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String username);
}
