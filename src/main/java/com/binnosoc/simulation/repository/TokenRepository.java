package com.binnosoc.simulation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binnosoc.simulation.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
	Optional<Token> findByToken(String token);
}
