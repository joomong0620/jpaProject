package com.jr.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jr.jpa.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
											// JpaRepository<엔티티 ,PK타입>
	
	Optional<Tag> findByTagName(String tagName);
	
}
