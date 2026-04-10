package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

	boolean existsByUsername(String username);
	Optional<MemberEntity> findByUsername(String username);
}
