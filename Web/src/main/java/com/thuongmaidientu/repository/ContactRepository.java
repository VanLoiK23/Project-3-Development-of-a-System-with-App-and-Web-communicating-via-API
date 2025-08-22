package com.thuongmaidientu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.ContactEntity;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
		
	@Modifying
	@Transactional
	@Query("UPDATE ContactEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);
}
